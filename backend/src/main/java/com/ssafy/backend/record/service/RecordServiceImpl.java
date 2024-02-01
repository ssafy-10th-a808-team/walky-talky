package com.ssafy.backend.record.service;

import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.mapping.ListMapping;
import com.ssafy.backend.record.dto.mapping.PointsMapping;
import com.ssafy.backend.record.dto.request.RequestRecordModify;
import com.ssafy.backend.record.dto.request.RequestRegistCommentDto;
import com.ssafy.backend.record.dto.request.RequestRegistImageDto;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.region.service.RegionService;
import com.ssafy.backend.scrapRecord.service.ScrapRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final RecordDetailRepository recordDetailRepository;

    private final RegionService regionService;

    private final S3UploadService s3UploadService;

    public Long startRecord(Long memberSeq) {
        Record record = Record.builder()
                .memberSeq(memberSeq)
                .build();

        Record returnRecord = recordRepository.save(record);

        return returnRecord.getSeq();
    }

    @Transactional
    public void registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto) throws WTException {
        Long recordSeq;
        try {
            // record table
            recordSeq = requestRegistRecordDto.getSeq();

            if (!validateRecord(recordSeq, memberSeq)) {
                throw new WTException("비정상적인 요청입니다.");
            }

            Record record = Record.builder()
                    .seq(recordSeq)
                    .memberSeq(memberSeq)
                    .title(requestRegistRecordDto.getTitle())
                    .duration(requestRegistRecordDto.getDuration())
                    .distance(requestRegistRecordDto.getDistance())
                    .starRating(requestRegistRecordDto.getStarRating())
                    .comment(requestRegistRecordDto.getComment())
                    .regionCd(requestRegistRecordDto.getRegionCd())
                    .build();

            recordRepository.save(record);
        } catch (Exception e) {
            throw new WTException("산책 기록 등록 오류입니다.");
        }

        try {
            // record detail table
            String[][] tmpPoint = requestRegistRecordDto.getPoints();
            List<RecordDetail> list = new ArrayList<>();

            for (String[] p : tmpPoint) {
                RecordDetail recordDetail = RecordDetail.builder()
                        .recordSeq(recordSeq)
                        .latitude(p[0])
                        .longitude(p[1])
                        .time(p[2])
                        .build();
                list.add(recordDetail);
            }

            recordDetailRepository.saveAll(list);
        } catch (Exception e) {
            throw new WTException("산책 기록 상세 등록 오류입니다.");
        }
    }

    @Transactional
    public Long registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto) throws WTException {
        try {
            Long recordSeq = requestRegistCommentDto.getSeq();

            if (!validateRecord(recordSeq, memberSeq)) {
                throw new WTException("비정상적인 요청입니다.");
            }

            RecordDetail recordDetail = RecordDetail.builder()
                    .recordSeq(recordSeq)
                    .pointComment(requestRegistCommentDto.getComment())
                    .latitude(requestRegistCommentDto.getLatitude())
                    .longitude(requestRegistCommentDto.getLongitude())
                    .build();

            return recordDetailRepository.save(recordDetail).getSeq();
        } catch (Exception e) {
            throw new WTException("산책 중 한줄평 등록에 실패하였습니다.");
        }
    }

    @Transactional
    public void modifyComment(Long memberSeq, Long recordDetailSeq, String comment) throws WTException {
        Optional<RecordDetail> recordDetailOptional = recordDetailRepository.findById(recordDetailSeq);

        if (recordDetailOptional.isEmpty()) {
            throw new WTException("산책 중 한줄평 수정에 실패하였습니다.");
        }

        RecordDetail recordDetail = recordDetailOptional.get();

        Long recordSeq = recordDetail.getRecordSeq();
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        try {
            recordDetail.updateComment(comment);
            recordDetailRepository.save(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 한줄평 수정에 실패하였습니다.");
        }
    }

    @Transactional
    public void deleteComment(Long memberSeq, Long recordDetailSeq) {
        Optional<RecordDetail> recordDetailOptional = recordDetailRepository.findById(recordDetailSeq);

        if (recordDetailOptional.isEmpty()) {
            throw new WTException("산책 중 한줄평 삭제에 실패하였습니다.");
        }

        RecordDetail recordDetail = recordDetailOptional.get();

        Long recordSeq = recordDetail.getRecordSeq();
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        try {
            recordDetailRepository.delete(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 한줄평 삭제에 실패하였습니다.");
        }
    }

    @Transactional
    public Long registImage(Long memberSeq, RequestRegistImageDto requestRegistImageDto) {
        Long recordSeq = requestRegistImageDto.getSeq();

        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        String url;

        try {
            url = s3UploadService.uploadRecordImg(requestRegistImageDto.getMultipartFile(), memberSeq, recordSeq);
        } catch (IOException e) {
            throw new WTException("사진 업로드에 실패하였습니다.");
        }

        try {
            RecordDetail recordDetail = RecordDetail.builder()
                    .recordSeq(recordSeq)
                    .url(url)
                    .latitude(requestRegistImageDto.getLatitude())
                    .longitude(requestRegistImageDto.getLongitude())
                    .build();

            return recordDetailRepository.save(recordDetail).getSeq();
        } catch (Exception e) {
            throw new WTException("산책 중 사진 등록에 실패하였습니다.");
        }
    }

    @Transactional
    public void modifyImage(Long memberSeq, Long recordDetailSeq, MultipartFile multipartFile) {
        Optional<RecordDetail> recordDetailOptional = recordDetailRepository.findById(recordDetailSeq);

        if (recordDetailOptional.isEmpty()) {
            throw new WTException("산책 중 사진 수정에 실패하였습니다.");
        }

        RecordDetail recordDetail = recordDetailOptional.get();

        Long recordSeq = recordDetail.getRecordSeq();
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        String existingUrl = recordDetail.getUrl();

        s3UploadService.deleteImg(existingUrl);

        String url;
        try {
            url = s3UploadService.uploadRecordImg(multipartFile, memberSeq, recordSeq);
        } catch (IOException e) {
            throw new WTException("사진 업로드에 실패하였습니다.");
        }

        try {
            recordDetail.updateUrl(url);
            recordDetailRepository.save(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 사진 수정에 실패하였습니다.");
        }
    }

    public void deleteImage(Long memberSeq, Long recordDetailSeq) {
        Optional<RecordDetail> recordDetailOptional = recordDetailRepository.findById(recordDetailSeq);

        if (recordDetailOptional.isEmpty()) {
            throw new WTException("산책 중 사진 삭제에 실패하였습니다.");
        }

        RecordDetail recordDetail = recordDetailOptional.get();

        Long recordSeq = recordDetail.getRecordSeq();
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        try {
            String existingUrl = recordDetail.getUrl();
            s3UploadService.deleteImg(existingUrl);

            recordDetailRepository.delete(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 사진 삭제에 실패하였습니다.");
        }

    }

    public List<ListMapping> list(Long memberSeq) throws WTException {
        return recordRepository.findResponseListDtoByMemberSeqAndIsDeletedFalse(memberSeq);
    }

    public List<ListMapping> list(List<Long> recordSeq) throws WTException {
        return recordRepository.findBySeqIn(recordSeq);
    }

    @Transactional
    public ResponseViewDto view(Long memberSeq, Long recordSeq) {
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        return view(recordSeq);
    }

    @Transactional
    public ResponseViewDto view(Long recordSeq) {
        ResponseViewDto responseViewDto = new ResponseViewDto();

        Optional<Record> recordOptional = recordRepository.findById(recordSeq);

        if (recordOptional.isEmpty()) {
            throw new WTException("산책 기록 상세 보기에 실패하였습니다.");
        }

        try {
            Record record = recordOptional.get();
            responseViewDto.setTitle(record.getTitle());
            responseViewDto.setDuration(record.getDuration());
            responseViewDto.setDistance(record.getDistance());
            responseViewDto.setStarRating(record.getStarRating());
            responseViewDto.setComment(record.getComment());

            String regionCode = record.getRegionCd();
            String address = regionService.findAddress(regionCode);
            responseViewDto.setAddress(address);

            responseViewDto.setStartTime(String.valueOf(record.getCreatedAt()));
        } catch (Exception e) {
            throw new WTException("산책 기록 상세 보기에 실패하였습니다.");
        }

        try {
            List<PointsMapping> pointsMappings = recordDetailRepository.findAllByRecordSeq(recordSeq);

            responseViewDto.setPoints(pointsMappings);
        } catch (Exception e) {
            throw new WTException("산책 기록 상세 보기에 실패하였습니다.");
        }

        return responseViewDto;
    }

    @Transactional
    public void modify(Long memberSeq, Long recordSeq, RequestRecordModify requestRecordModify) {
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        Optional<Record> recordOptional = recordRepository.findById(recordSeq);

        if (recordOptional.isEmpty()) {
            throw new WTException("한줄평 및 별점 수정에 실패하였습니다.");
        }

        Record record = recordOptional.get();

        try {
            record.update(requestRecordModify.getComment(), requestRecordModify.getStarRating());
            recordRepository.save(record);
        } catch (Exception e) {
            throw new WTException("한줄평 및 별점 수정에 실패하였습니다.");
        }
    }

    public void delete(Long memberSeq, Long recordSeq) {
        if (!validateRecord(recordSeq, memberSeq)) {
            throw new WTException("비정상적인 요청입니다.");
        }

        Optional<Record> recordOptional = recordRepository.findById(recordSeq);

        if (recordOptional.isEmpty()) {
            throw new WTException("한줄평 및 별점 수정에 실패하였습니다.");
        }
        Record record = recordOptional.get();

        try {
            record.delete(record);
            recordRepository.save(record);
        } catch (Exception e) {
            throw new WTException("한줄평 및 별점 수정에 실패하였습니다.");
        }
    }

    public boolean isRecordCreatedByMember(Long recordSeq, Long memberSeq) throws WTException {
        return recordRepository.existsBySeqAndMemberSeqAndIsDeletedFalse(recordSeq, memberSeq);
    }

    @Override
    public boolean isRecordDeleted(Long recordSeq) throws WTException {
        return recordRepository.existsBySeqAndIsDeletedTrue(recordSeq);
    }

    @Override
    public boolean isRecordExist(Long recordSeq) throws WTException {
        return recordRepository.existsById(recordSeq);
    }

    private boolean validateRecord(Long recordSeq, Long memberSeq) {
        if (!recordRepository.existsById(recordSeq)) {
            // 입력 받은 기록 식별번호에 해당하는 기록이 없는 경우이므로
            // 이는 사용자가 시작을 누르지 않은 비정상 요청임
            return false;
        }

        if (!memberSeq.equals(recordRepository.findMemberSeqBySeq(recordSeq).getMemberSeq())) {
            // 기존 저장되어있던 기록 식별번호를 등록한 사용자와
            // 지금 가져온 사용자 아이디에 해당하는 사람이 다르면 비정상 요청임
            return false;
        }

        return true;
    }

}

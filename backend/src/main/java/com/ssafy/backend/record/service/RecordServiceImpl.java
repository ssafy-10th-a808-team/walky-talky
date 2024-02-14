package com.ssafy.backend.record.service;

import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.service.MemberService;
import com.ssafy.backend.record.domain.Dislike;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.mapping.DislikeRecordMapping;
import com.ssafy.backend.record.dto.mapping.PointsMapping;
import com.ssafy.backend.record.dto.request.RequestRecordModify;
import com.ssafy.backend.record.dto.request.RequestRegistCommentDto;
import com.ssafy.backend.record.dto.request.RequestRegistImageDto;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.dto.response.ResponseListDto;
import com.ssafy.backend.record.dto.response.ResponsePointDto;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.record.repository.DislikeRepository;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import com.ssafy.backend.region.service.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final RecordDetailRepository recordDetailRepository;

    private final DislikeRepository dislikeRepository;

    private final RegionService regionService;

    private final MemberService memberService;

    private final S3UploadService s3UploadService;

    public Long startRecord(Long memberSeq) {
        Record record = Record.builder().memberSeq(memberSeq).build();
        Record returnRecord = recordRepository.save(record);

        return returnRecord.getSeq();
    }

    @Transactional
    public void registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto) throws WTException {
        if (requestRegistRecordDto == null) {
            throw new WTException("산책 기록 등록 오류입니다.");
        }

        Long recordSeq = requestRegistRecordDto.getSeq();

        validateRecord(recordSeq, memberSeq);

        try {
            // record table
            Record record = Record.builder().seq(recordSeq).memberSeq(memberSeq).title(requestRegistRecordDto.getTitle()).duration(requestRegistRecordDto.getDuration()).distance(requestRegistRecordDto.getDistance()).starRating(requestRegistRecordDto.getStarRating()).comment(requestRegistRecordDto.getComment()).regionCd(requestRegistRecordDto.getRegionCd()).build();

            recordRepository.save(record);
        } catch (Exception e) {
            throw new WTException("산책 기록 등록 오류입니다.");
        }

        try {
            // record detail table
            String[][] tmpPoint = requestRegistRecordDto.getPoints();
            List<RecordDetail> list = new ArrayList<>();

            for (String[] p : tmpPoint) {
                RecordDetail recordDetail = RecordDetail.builder().recordSeq(recordSeq).latitude(p[0]).longitude(p[1]).time(p[2]).build();
                list.add(recordDetail);
            }

            recordDetailRepository.saveAll(list);
        } catch (Exception e) {
            throw new WTException("산책 기록 상세 등록 오류입니다.");
        }
    }

    @Transactional
    public Long registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto) throws WTException {
        if (requestRegistCommentDto == null) {
            throw new WTException("산책 중 한줄평 등록에 실패하였습니다.");
        }

        Long recordSeq = requestRegistCommentDto.getSeq();

        validateRecord(recordSeq, memberSeq);

        try {
            RecordDetail recordDetail = RecordDetail.builder().recordSeq(recordSeq).pointComment(requestRegistCommentDto.getComment()).latitude(requestRegistCommentDto.getLatitude()).longitude(requestRegistCommentDto.getLongitude()).build();

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

        validateRecord(recordSeq, memberSeq);

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

        validateRecord(recordSeq, memberSeq);

        try {
            recordDetailRepository.delete(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 한줄평 삭제에 실패하였습니다.");
        }
    }

    @Transactional
    public Long registImage(Long memberSeq, RequestRegistImageDto requestRegistImageDto) {
        Long recordSeq = requestRegistImageDto.getSeq();

        validateRecord(recordSeq, memberSeq);

        String url;

        try {
            url = s3UploadService.uploadRecordImg(requestRegistImageDto.getMultipartFile(), memberSeq, recordSeq);
        } catch (IOException e) {
            throw new WTException("사진 업로드에 실패하였습니다.");
        }

        try {
            RecordDetail recordDetail = RecordDetail.builder().recordSeq(recordSeq).url(url).latitude(requestRegistImageDto.getLatitude()).longitude(requestRegistImageDto.getLongitude()).build();

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

        validateRecord(recordSeq, memberSeq);

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

        validateRecord(recordSeq, memberSeq);

        try {
            String existingUrl = recordDetail.getUrl();
            s3UploadService.deleteImg(existingUrl);

            recordDetailRepository.delete(recordDetail);
        } catch (Exception e) {
            throw new WTException("산책 중 사진 삭제에 실패하였습니다.");
        }

    }

    @Transactional
    public List<ResponseListDto> list(Long memberSeq) throws WTException {
        try {
            List<Record> recordList = recordRepository.findByMemberSeqAndIsDeletedFalseOrderByCreatedAtDesc(memberSeq);

            return listMapping(recordList);
        } catch (Exception e) {
            throw new WTException("목록 불러오기에 실패하였습니다.");
        }
    }

    @Transactional
    public List<ResponseListDto> list(List<Long> recordSeq) throws WTException {
        try {
            List<Record> recordList = recordRepository.findBySeqIn(recordSeq);

            return listMapping(recordList);
        } catch (Exception e) {
            throw new WTException("스크랩 목록 불러오기에 실패하였습니다.");
        }
    }

    @Transactional
    public ResponseViewDto view(Long memberSeq, Long recordSeq) {
        validateRecord(recordSeq, memberSeq);

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
        validateRecord(recordSeq, memberSeq);

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
        validateRecord(recordSeq, memberSeq);

        Optional<Record> recordOptional = recordRepository.findById(recordSeq);

        if (recordOptional.isEmpty()) {
            throw new WTException("한줄평 및 별점 수정에 실패하였습니다.");
        }
        Record record = recordOptional.get();

        try {
            record.delete();
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

    @Override
    public List<ResponseListDto> recommendTown(Long memberSeq) throws WTException {
        String regionCd = memberService.getRegionCd(memberSeq);

        try {
            List<Long> dislikeList = new ArrayList<>(dislikeRepository.findRecordSeqByMemberSeq(memberSeq).stream().map(DislikeRecordMapping::getRecordSeq).toList());

            dislikeList.add(0L);

            List<Record> recordList = recordRepository.findByRegionCdAndSeqNotInAndMemberSeqNotAndIsDeletedFalse(regionCd, dislikeList, memberSeq);

            return listMappingRecommand(recordList);
        } catch (Exception e) {
            throw new WTException("동네 기반 코스 추천에 실패하였습니다.");
        }

    }

    @Override
    public List<ResponseListDto> recommendInfo(Long memberSeq) throws WTException {
        List<Long> memberList;
        try {
            memberList = memberService.getSimilarMemberList(memberSeq);

            memberList.add(0L);
        } catch (Exception e) {
            throw new WTException("사용자 정보 가져오기에 실패했습니다.");
        }

        try {
            List<Long> dislikeList = new ArrayList<>(dislikeRepository.findRecordSeqByMemberSeq(memberSeq).stream().map(DislikeRecordMapping::getRecordSeq).toList());

            dislikeList.add(0L);

            List<Record> recordList = recordRepository.findByMemberSeqInAndSeqNotInAndIsDeletedFalse(memberList, dislikeList);

            return listMappingRecommand(recordList);
        } catch (Exception e) {
            throw new WTException("사용자기반 코스 추천에 실패하였습니다.");
        }
    }

    @Override
    public void dislike(Long recordSeq, Long memberSeq) throws WTException {
        if (!isRecordExist(recordSeq)) {
            throw new WTException("존재하지 않는 기록입니다.");
        }

        if (isRecordDeleted(recordSeq)) {
            throw new WTException("삭제된 기록입니다.");
        }

        try {
            Dislike dislike = Dislike.builder().recordSeq(recordSeq).memberSeq(memberSeq).build();
            dislikeRepository.save(dislike);
        } catch (Exception e) {
            throw new WTException("싫어요에 실패하였습니다.");
        }
    }

    @Override
    public List<ResponsePointDto> clone(Long recordSeq, Long memberSeq) throws WTException {
        List<ResponsePointDto> responsePointDtoList;

        List<PointsMapping> list = recordDetailRepository.findAllByRecordSeq(recordSeq);
        if (list.isEmpty()) {
            throw new WTException("해당하는 산책 기록이 없습니다.");
        }

        try {
            responsePointDtoList = list.stream()
                    .map(mapping -> new ResponsePointDto(mapping.getLatitude(), mapping.getLongitude()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WTException("따라 뛰기에 실패하였습니다.");
        }

        return responsePointDtoList;
    }

    private void validateRecord(Long recordSeq, Long memberSeq) throws WTException {
        if (!recordRepository.existsById(recordSeq)) {
            // 입력 받은 기록 식별번호에 해당하는 기록이 없는 경우이므로
            // 이는 사용자가 시작을 누르지 않은 비정상 요청임
            throw new WTException("비정상적인 요청입니다.");
        }
    }

    private List<ResponseListDto> listMapping(List<Record> recordList) {
        List<ResponseListDto> list = new ArrayList<>();
        for (Record record : recordList) {
            try {
                ResponseListDto responseListDto = new ResponseListDto();

                responseListDto.setRecordSeq(record.getSeq());
                responseListDto.setTitle(record.getTitle());
                responseListDto.setPoints(view(record.getSeq()).getPoints());
                responseListDto.setStarRating(record.getStarRating());
                responseListDto.setComment(record.getComment());
                responseListDto.setDistance(record.getDistance());
                responseListDto.setDuration(record.getDuration());

                list.add(responseListDto);
            } catch (Exception e) {
                continue;
            }

        }
        return list;
    }


    private List<ResponseListDto> listMappingRecommand(List<Record> recordList) {
        List<ResponseListDto> list = new ArrayList<>();

        Collections.shuffle(recordList);

        int count = 0;

        for (Record record : recordList) {
            if (count == 5) {
                break;
            }
            try {
                ResponseListDto responseListDto = new ResponseListDto();

                responseListDto.setRecordSeq(record.getSeq());
                responseListDto.setTitle(record.getTitle());
                responseListDto.setPoints(view(record.getSeq()).getPoints());
                responseListDto.setStarRating(record.getStarRating());
                responseListDto.setComment(record.getComment());
                responseListDto.setDistance(record.getDistance());
                responseListDto.setDuration(record.getDuration());

                list.add(responseListDto);
                count++;
            } catch (Exception e) {
                continue;
            }

        }
        return list;
    }

}

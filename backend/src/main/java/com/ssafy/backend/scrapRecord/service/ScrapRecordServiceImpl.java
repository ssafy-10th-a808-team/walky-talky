package com.ssafy.backend.scrapRecord.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.record.dto.mapping.ListMapping;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import com.ssafy.backend.record.service.RecordService;
import com.ssafy.backend.scrapRecord.domain.Scrap;
import com.ssafy.backend.scrapRecord.dto.mapping.RecordSeqMapping;
import com.ssafy.backend.scrapRecord.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapRecordServiceImpl implements ScrapRecordService {

    private final ScrapRepository scrapRepository;

    private final RecordService recordService;

    @Override
    public void scrap(Long recordSeq, Long memberSeq) throws WTException {
        if (!recordService.isRecordExist(recordSeq)) {
            throw new WTException("존재하지 않는 기록입니다.");
        }

        if (recordService.isRecordDeleted(recordSeq)) {
            throw new WTException("삭제된 기록입니다.");
        }

        if (recordService.isRecordCreatedByMember(recordSeq, memberSeq)) {
            throw new WTException("자신의 기록은 스크랩 할 수 없습니다.");
        }

        if (scrapRepository.existsByRecordSeqAndMemberSeq(recordSeq, memberSeq)) {
            throw new WTException("이미 스크랩한 기록입니다.");
        }

        try {
            Scrap scrap = Scrap.builder()
                    .recordSeq(recordSeq)
                    .memberSeq(memberSeq)
                    .build();
            scrapRepository.save(scrap);
        } catch (Exception e) {
            throw new WTException("스크랩에 실패하였습니다.");
        }

    }

    @Override
    public void cancel(Long recordSeq, Long memberSeq) throws WTException {
        if (!recordService.isRecordExist(recordSeq)) {
            throw new WTException("존재하지 않는 기록입니다.");
        }

        if (recordService.isRecordDeleted(recordSeq)) {
            throw new WTException("삭제된 기록입니다.");
        }

        Scrap scrap = scrapRepository.findByRecordSeqAndMemberSeq(recordSeq, memberSeq);

        try {
            scrapRepository.delete(scrap);
        } catch (Exception e) {
            throw new WTException("스크랩 취소에 실패하였습니다.");
        }
    }

    public List<ListMapping> list(Long memberSeq) throws WTException {
        try {
            List<RecordSeqMapping> recordSeqMappingList = scrapRepository.findRecordSeqByMemberSeq(memberSeq);

            List<Long> recordSeqList = recordSeqMappingList.stream()
                    .map(RecordSeqMapping::getRecordSeq)
                    .collect(Collectors.<Long>toList());

            return recordService.list(recordSeqList);
        } catch (Exception e) {
            throw new WTException("스크랩 목록 불러오기에 실패하였습니다.");
        }
    }

    @Override
    public int getScrapcount(Long recordSeq) throws WTException {
        return scrapRepository.countAllByRecordSeq(recordSeq);
    }

    @Override
    public boolean getIsScraped(Long recordSeq, Long memberSeq) throws WTException {
        return scrapRepository.existsByRecordSeqAndMemberSeq(recordSeq, memberSeq);
    }

    @Override
    public ResponseViewDto view(Long recordSeq, Long memberSeq) throws WTException {
        if (!recordService.isRecordExist(recordSeq)) {
            throw new WTException("존재하지 않는 기록입니다.");
        }

        if (recordService.isRecordDeleted(recordSeq)) {
            throw new WTException("삭제된 기록입니다.");
        }

        if (scrapRepository.existsByRecordSeqAndMemberSeq(recordSeq, memberSeq)) {
            throw new WTException("자신이 스크랩한 기록만 볼 수 있습니다.");
        }

        return recordService.view(recordSeq);
    }
}

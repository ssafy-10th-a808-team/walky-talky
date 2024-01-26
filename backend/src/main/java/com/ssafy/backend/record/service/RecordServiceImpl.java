package com.ssafy.backend.record.service;

import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final RecordDetailRepository recordDetailRepository;

    private final MemberRepository memberRepository;

    public Long startRecord(Long memberSeq){
        Record record = Record.builder()
                .memberSeq(memberSeq)
                .build();

        Record returnRecord = recordRepository.save(record);

        return returnRecord.getSeq();
    }

    public boolean registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto) {
        // record table
        Long seq = requestRegistRecordDto.getSeq();

        if(!recordRepository.existsById(seq)){
            // 입력 받은 기록 식별번호에 해당하는 기록이 없는 경우이므로
            // 이는 사용자가 시작을 누르지 않은 비정상 요청임
            return false;
        }


        if(!memberSeq.equals(recordRepository.findMemberSeqBySeq(seq).getMemberSeq())){
            // 기존 저장되어있던 기록 식별번호를 등록한 사용자와
            // 지금 가져온 사용자 아이디에 해당하는 사람이 다르면 비정상 요청임
            return false;
        }

        Record record = Record.builder()
                .seq(seq)
                .memberSeq(memberSeq)
                .title(requestRegistRecordDto.getTitle())
                .duration(requestRegistRecordDto.getDuration())
                .distance(Double.valueOf(requestRegistRecordDto.getDistance()))
                .starRating(requestRegistRecordDto.getStarRating())
                .comment(requestRegistRecordDto.getComment())
                .build();

        recordRepository.save(record);

        // record detail table
        String[][] tmpPoint = requestRegistRecordDto.getPoints();
        List<RecordDetail> list = new ArrayList<>();

        for(String[] p:tmpPoint){
            RecordDetail recordDetail = RecordDetail.builder()
                    .recordSeq(seq)
                    .latitude(p[0])
                    .longitude(p[1])
                    .time(p[2])
                    .build();
            list.add(recordDetail);
        }

        recordDetailRepository.saveAll(list);

        return true;
    }

}

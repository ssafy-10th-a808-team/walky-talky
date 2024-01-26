package com.ssafy.backend.record.service;

import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.record.domain.Point;
import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.repository.RecordDetailRepository;
import com.ssafy.backend.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final RecordDetailRepository recordDetailRepository;

    private final MemberRepository memberRepository;

    public Long startRecord(String memberId){
        Long memberSeq = memberRepository.findByMemberId(memberId).getSeq();

        Record record = Record.builder()
                .memberSeq(memberSeq)
                .build();

        Record returnRecord = recordRepository.save(record);

        return returnRecord.getSeq();
    }

    public void registRecord(String memberId, RequestRegistRecordDto requestRegistRecordDto) {
        // record table
        Long memberSeq = memberRepository.findByMemberId(memberId).getSeq();

        Record record = Record.builder()
                .title(requestRegistRecordDto.getTitle())
                .comment(requestRegistRecordDto.getComment())
                .duration(Duration.parse(requestRegistRecordDto.getDuration()))
                .distance(Double.valueOf(requestRegistRecordDto.getDistance()))
                .starRating(requestRegistRecordDto.getStarRating())
                .build();

        recordRepository.save(record);

        // record detail table
        String[][] tmpPoint = requestRegistRecordDto.getPoints();
        List<RecordDetail> list = new ArrayList<>();

        Point point = new Point();

        for(String[] p:tmpPoint){
            point.setLat(Double.valueOf(p[0]));
            point.setLng(Double.valueOf(p[1]));
            RecordDetail recordDetail = RecordDetail.builder()
                    .point(point)
                    .time(p[2])
                    .build();
        }


        recordDetailRepository.saveAll(list);

    }

}

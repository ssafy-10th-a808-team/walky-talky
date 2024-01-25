package com.ssafy.backend.record.service;

import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    public void registRecord(String memberId, RequestRegistRecordDto requestRegistRecordDto){
        // Todo : record table이랑 record_detail table에 data 전부 저장하는 코드
    }

}

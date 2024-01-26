package com.ssafy.backend.record.service;

import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;

public interface RecordService {

    Long startRecord(Long memberSeq);

    boolean registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto);

}

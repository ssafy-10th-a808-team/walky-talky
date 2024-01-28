package com.ssafy.backend.record.service;

import com.ssafy.backend.record.dto.request.RequestRecordModify;
import com.ssafy.backend.record.dto.request.RequestRegistCommentDto;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.dto.response.ResponseListDto;

import java.util.List;

public interface RecordService {

    Long startRecord(Long memberSeq);

    boolean registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto);

    boolean registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto);

    List<ResponseListDto> list(Long memberSeq);

    boolean modify(Long memberSeq, Long recordSeq, RequestRecordModify requestRecordModify);

    boolean delete(Long memberSeq, Long recordSeq);

}

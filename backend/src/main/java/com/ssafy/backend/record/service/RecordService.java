package com.ssafy.backend.record.service;

import com.ssafy.backend.record.dto.request.*;
import com.ssafy.backend.record.dto.response.ResponseListDto;
import com.ssafy.backend.record.dto.response.ResponseViewDto;

import java.util.List;

public interface RecordService {

    Long startRecord(Long memberSeq);

    boolean registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto);

    boolean registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto);

    Long registImage(Long memberSeq, RequestRegistImageDto requestRegistImageDto);

    boolean modifyImage(Long memberSeq, RequestModifyImageDto requestModifyImageDto);

    List<ResponseListDto> list(Long memberSeq);

    ResponseViewDto view(Long recordSeq);

    boolean modify(Long memberSeq, Long recordSeq, RequestRecordModify requestRecordModify);

    boolean delete(Long memberSeq, Long recordSeq);

}

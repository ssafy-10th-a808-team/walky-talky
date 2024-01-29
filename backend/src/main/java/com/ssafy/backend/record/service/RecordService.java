package com.ssafy.backend.record.service;

import com.ssafy.backend.record.dto.request.RequestRecordModify;
import com.ssafy.backend.record.dto.request.RequestRegistCommentDto;
import com.ssafy.backend.record.dto.request.RequestRegistImageDto;
import com.ssafy.backend.record.dto.request.RequestRegistRecordDto;
import com.ssafy.backend.record.dto.response.ResponseListDto;
import com.ssafy.backend.record.dto.response.ResponseViewDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {

    Long startRecord(Long memberSeq);

    boolean registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto);

    Long registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto);

    boolean modifyComment(Long memberSeq, Long recordDetailSeq, String comment);

    boolean deleteComment(Long memberSeq, Long recordDetailSeq);

    Long registImage(Long memberSeq, RequestRegistImageDto requestRegistImageDto);

    boolean modifyImage(Long memberSeq, Long recordDetailSeq, MultipartFile multipartFile);

    boolean deleteImage(Long memberSeq, Long recordDetailSeq);

    List<ResponseListDto> list(Long memberSeq);

    ResponseViewDto view(Long recordSeq);

    boolean modify(Long memberSeq, Long recordSeq, RequestRecordModify requestRecordModify);

    boolean delete(Long memberSeq, Long recordSeq);

}

package com.ssafy.backend.record.service;

import com.ssafy.backend.global.error.WTException;
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

    void registRecord(Long memberSeq, RequestRegistRecordDto requestRegistRecordDto) throws WTException;

    Long registComment(Long memberSeq, RequestRegistCommentDto requestRegistCommentDto) throws WTException;

    void modifyComment(Long memberSeq, Long recordDetailSeq, String comment) throws WTException;

    void deleteComment(Long memberSeq, Long recordDetailSeq) throws WTException;

    Long registImage(Long memberSeq, RequestRegistImageDto requestRegistImageDto) throws WTException;

    void modifyImage(Long memberSeq, Long recordDetailSeq, MultipartFile multipartFile) throws WTException;

    void deleteImage(Long memberSeq, Long recordDetailSeq) throws WTException;

    List<ResponseListDto> list(Long memberSeq);

    ResponseViewDto view(Long memberSeq, Long recordSeq) throws WTException;

    void modify(Long memberSeq, Long recordSeq, RequestRecordModify requestRecordModify) throws WTException;

    void delete(Long memberSeq, Long recordSeq) throws WTException;

}
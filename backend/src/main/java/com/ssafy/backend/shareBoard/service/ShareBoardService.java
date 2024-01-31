package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.*;

import java.util.List;

public interface ShareBoardService {
    void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException;

    List<ResponseShareBoardDto> listContent() throws WTException;

    List<ResponseRecordDto> listRecord() throws WTException;

    List<ResponseLikeDto> listLike(Long memberSeq) throws WTException;

    List<ResponseScrapDto> listScrap(Long memberSeq) throws WTException;

    ResponseShareBoardContentDto viewContent(Long shareBoardSeq) throws WTException;

    ResponseRecordDto viewRecord(Long shareBoardSeq) throws WTException;

    List<ResponseCommentDto> viewComment(Long shareBoardSeq) throws WTException;

    ResponseLikeDto viewLike(Long shareBoardSeq, Long memberSeq) throws WTException;

    ResponseScrapDto viewScrap(Long shareBoardSeq, Long memberSeq) throws WTException;
}

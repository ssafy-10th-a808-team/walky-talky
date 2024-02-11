package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardModifyDto;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.*;
import com.ssafy.backend.shareBoardCommet.dto.response.ResponseShareBoardCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShareBoardService {
    void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException;

    int getTotalCount();

    List<ResponseShareBoardDto> listContent(Pageable pageable) throws WTException;

    List<ResponseRecordDto> listRecord(Pageable pageable) throws WTException;

    List<ResponseLikeDto> listLike(Long memberSeq, Pageable pageable) throws WTException;

    List<ResponseScrapDto> listScrap(Long memberSeq, Pageable pageable) throws WTException;

    ResponseShareBoardContentDto viewContent(Long shareBoardSeq) throws WTException;

    ResponseRecordDto viewRecord(Long shareBoardSeq) throws WTException;

    List<ResponseShareBoardCommentDto> viewComment(Long shareBoardSeq) throws WTException;

    ResponseLikeDto viewLike(Long shareBoardSeq, Long memberSeq) throws WTException;

    ResponseScrapDto viewScrap(Long shareBoardSeq, Long memberSeq) throws WTException;

    void modify(Long memberSeq, Long shareBoardSeq, RequestShareBoardModifyDto requestShareBoardModifyDto) throws WTException;

    void delete(Long memberSeq, Long shareBoardSeq) throws WTException;

    void like(Long shareBoardSeq, Long memberSeq) throws WTException;

    void likeCancel(Long shareBoardSeq, Long memberSeq) throws WTException;

}

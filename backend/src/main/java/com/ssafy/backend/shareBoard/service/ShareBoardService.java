package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseShareBoardListDto;

import java.util.List;

public interface ShareBoardService {
    void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException;

    List<ResponseShareBoardListDto> list() throws WTException;
}

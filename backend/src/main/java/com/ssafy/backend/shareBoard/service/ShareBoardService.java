package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;

public interface ShareBoardService {
    void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException;
}

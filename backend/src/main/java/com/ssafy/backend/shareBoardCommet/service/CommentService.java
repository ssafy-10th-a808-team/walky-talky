package com.ssafy.backend.shareBoardCommet.service;

import com.ssafy.backend.global.error.WTException;

public interface CommentService {

    void commentWrite(Long shareBoardSeq, Long memberSeq, String content) throws WTException;

    void commentModify(Long shareBoardSeq, Long commentSeq, Long memberSeq, String content) throws WTException;

    void commentDelete(Long shareBoardSeq, Long commentSeq, Long memberSeq) throws WTException;

}

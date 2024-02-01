package com.ssafy.backend.shareBoardCommet.service;

import com.ssafy.backend.global.error.WTException;

public interface CommentService {

    void commentWrite(Long seq, Long memberSeq, String content) throws WTException;

    void commentModify(Long seq, Long commentSeq, Long memberSeq, String content) throws WTException;

    void commentDelete(Long seq, Long commentSeq, Long memberSeq) throws WTException;

}

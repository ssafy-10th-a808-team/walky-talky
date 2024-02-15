package com.ssafy.backend.shareBoardCommet.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoardCommet.domain.ShareBoardComment;

import java.util.List;

public interface ShareBoardCommentService {

    void commentWrite(Long seq, Long memberSeq, String content) throws WTException;

    void commentModify(Long seq, Long commentSeq, Long memberSeq, String content) throws WTException;

    void commentDelete(Long seq, Long commentSeq, Long memberSeq) throws WTException;

    int getCommentCount(Long seq) throws WTException;

    List<ShareBoardComment> getComment(Long shareBoardSeq) throws WTException;
}

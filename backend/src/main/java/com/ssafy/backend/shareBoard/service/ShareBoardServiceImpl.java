package com.ssafy.backend.shareBoard.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.record.repository.ShareBoardLikeRepository;
import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.dto.request.RequestShareBoardWriteDto;
import com.ssafy.backend.shareBoard.repository.ShareBoardCommentRepository;
import com.ssafy.backend.shareBoard.repository.ShareBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareBoardServiceImpl implements ShareBoardService {

    private final ShareBoardRepository shareBoardRepository;

    private final ShareBoardCommentRepository shareBoardCommentRepository;

    private final ShareBoardLikeRepository shareBoardLikeRepository;

    public void write(Long memberSeq, RequestShareBoardWriteDto requestShareBoardWriteDto) throws WTException {
        try {
            ShareBoard shareBoard = ShareBoard.builder()
                    .memberSeq(memberSeq)
                    .recordSeq(requestShareBoardWriteDto.getRecordSeq())
                    .title(requestShareBoardWriteDto.getTitle())
                    .content(requestShareBoardWriteDto.getContent())
                    .build();

            shareBoardRepository.save(shareBoard);
        } catch (Exception e) {
            throw new WTException(e.getMessage()); // Todo : 개발 끝나고 고치기
        }
    }

}

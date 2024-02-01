package com.ssafy.backend.shareBoardCommet.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoardCommet.domain.ShareBoardComment;
import com.ssafy.backend.shareBoardCommet.repository.ShareBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareBoardCommentServiceImpl implements ShareBoardCommentService {

    private final ShareBoardCommentRepository shareBoardCommentRepository;

    @Override
    public void commentWrite(Long shareBoardSeq, Long memberSeq, String content) throws WTException {
        try {
            ShareBoardComment shareBoardComment = ShareBoardComment.builder()
                    .shareBoardSeq(shareBoardSeq)
                    .memberSeq(memberSeq)
                    .content(content)
                    .build();
            shareBoardCommentRepository.save(shareBoardComment);
        } catch (Exception e) {
            throw new WTException("댓글 등록 오류"); // Todo : 고치기
        }
    }

    @Override
    public void commentModify(Long shareBoardSeq, Long commentSeq, Long memberSeq, String content) throws WTException {
        Optional<ShareBoardComment> shareBoardCommentOptional = shareBoardCommentRepository.findById(commentSeq);

        if (shareBoardCommentOptional.isEmpty()) {
            throw new WTException("댓글 수정 오류");
        }

        ShareBoardComment shareBoardComment = shareBoardCommentOptional.get();

        if (!Objects.equals(shareBoardComment.getMemberSeq(), memberSeq) || !Objects.equals(shareBoardComment.getShareBoardSeq(), shareBoardSeq)) {
            throw new WTException("댓글 수정 오류입니다.");
        }

        if (shareBoardComment.isDeleted()) {
            throw new WTException("이미 삭제된 댓글입니다.");
        }

        try {
            shareBoardComment.update(content);
            shareBoardCommentRepository.save(shareBoardComment);
        } catch (Exception e) {
            throw new WTException("댓글 수정 오류.");
        }
    }

    @Override
    public void commentDelete(Long shareBoardSeq, Long commentSeq, Long memberSeq) throws WTException {
        Optional<ShareBoardComment> shareBoardCommentOptional = shareBoardCommentRepository.findById(commentSeq);

        if (shareBoardCommentOptional.isEmpty()) {
            throw new WTException("댓글 삭제 오류");
        }

        ShareBoardComment shareBoardComment = shareBoardCommentOptional.get();

        if (!Objects.equals(shareBoardComment.getMemberSeq(), memberSeq) || !Objects.equals(shareBoardComment.getShareBoardSeq(), shareBoardSeq)) {
            throw new WTException("댓글 삭제 오류입니다.");
        }

        if (shareBoardComment.isDeleted()) {
            throw new WTException("이미 삭제된 댓글입니다.");
        }

        try {
            shareBoardComment.delete(shareBoardComment);
            shareBoardCommentRepository.save(shareBoardComment);
        } catch (Exception e) {
            throw new WTException("댓글 삭제 오류.");
        }
    }
}

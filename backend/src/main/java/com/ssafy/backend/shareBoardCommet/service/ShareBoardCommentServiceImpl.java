package com.ssafy.backend.shareBoardCommet.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.shareBoardCommet.domain.ShareBoardComment;
import com.ssafy.backend.shareBoardCommet.repository.ShareBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new WTException("댓글 등록에 실패하였습니다.");
        }
    }

    @Override
    public void commentModify(Long shareBoardSeq, Long commentSeq, Long memberSeq, String content) throws WTException {
        Optional<ShareBoardComment> shareBoardCommentOptional = shareBoardCommentRepository.findById(commentSeq);

        if (shareBoardCommentOptional.isEmpty()) {
            throw new WTException("댓글 수정에 실패하였습니다.");
        }

        ShareBoardComment shareBoardComment = shareBoardCommentOptional.get();

        if (!Objects.equals(shareBoardComment.getMemberSeq(), memberSeq)) {
            throw new WTException("자신의 댓글만 수정할 수 있습니다.");
        }

        if (!Objects.equals(shareBoardComment.getShareBoardSeq(), shareBoardSeq)) {
            throw new WTException("댓글 수정에 실패하였습니다.");
        }

        if (shareBoardComment.isDeleted()) {
            throw new WTException("이미 삭제된 댓글입니다.");
        }

        try {
            shareBoardComment.update(content);
            shareBoardCommentRepository.save(shareBoardComment);
        } catch (Exception e) {
            throw new WTException("댓글 수정에 실패하였습니다.");
        }
    }

    @Override
    public void commentDelete(Long shareBoardSeq, Long commentSeq, Long memberSeq) throws WTException {
        Optional<ShareBoardComment> shareBoardCommentOptional = shareBoardCommentRepository.findById(commentSeq);

        if (shareBoardCommentOptional.isEmpty()) {
            throw new WTException("댓글 삭제에 실패하였습니다.");
        }

        ShareBoardComment shareBoardComment = shareBoardCommentOptional.get();

        if (!Objects.equals(shareBoardComment.getMemberSeq(), memberSeq)) {
            throw new WTException("자신의 댓글만 삭제할 수 있습니다.");
        }

        if (!Objects.equals(shareBoardComment.getShareBoardSeq(), shareBoardSeq)) {
            throw new WTException("댓글 삭제에 실패하였습니다.");
        }

        if (shareBoardComment.isDeleted()) {
            throw new WTException("이미 삭제된 댓글입니다.");
        }

        try {
            shareBoardComment.delete(shareBoardComment);
            shareBoardCommentRepository.save(shareBoardComment);
        } catch (Exception e) {
            throw new WTException("댓글 삭제에 실패하였습니다.");
        }
    }

    @Override
    public int getCommentCount(Long seq) throws WTException {
        return shareBoardCommentRepository.countByShareBoardSeqAndIsDeletedFalse(seq);
    }

    @Override
    public List<ShareBoardComment> getComment(Long shareBoardSeq) throws WTException {
        return shareBoardCommentRepository.findAllByShareBoardSeqAndIsDeletedFalse(shareBoardSeq);
    }
}

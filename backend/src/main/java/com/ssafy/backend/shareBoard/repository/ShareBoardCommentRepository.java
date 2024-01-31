package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareBoardCommentRepository extends JpaRepository<ShareBoardComment, Long> {

    int countByShareBoardSeqAndIsDeletedFalse(Long shareBoardSeq);

    List<ShareBoardComment> findAllByShareBoardSeqAndIsDeletedFalse(Long shareBoardSeq);


}

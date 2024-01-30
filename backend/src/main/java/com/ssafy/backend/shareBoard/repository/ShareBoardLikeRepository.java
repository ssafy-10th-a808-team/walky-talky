package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardLikeRepository extends JpaRepository<ShareBoardLike, Long> {

    int countAllByShareBoardSeq(Long shareBoardSeq);

    boolean existsByShareBoardSeqAndMemberSeq(Long shareBoardSeq, Long memberSeq);
}

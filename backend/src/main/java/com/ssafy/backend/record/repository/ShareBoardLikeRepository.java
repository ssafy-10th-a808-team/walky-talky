package com.ssafy.backend.record.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardLikeRepository extends JpaRepository<ShareBoardLike, Long> {
}

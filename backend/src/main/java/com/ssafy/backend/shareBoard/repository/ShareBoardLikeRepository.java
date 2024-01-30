package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardLikeRepository extends JpaRepository<ShareBoardLike, Long> {
}

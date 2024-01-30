package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardRepository extends JpaRepository<ShareBoard, Long> {
}

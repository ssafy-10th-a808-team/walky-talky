package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardMemberMapping;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardScrapMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareBoardRepository extends JpaRepository<ShareBoard, Long> {
    List<ShareBoard> findAllByIsDeletedFalse();

    List<ShareBoardMemberMapping> findSeqAndMemberSeqByIsDeletedFalse();

    List<ShareBoardScrapMapping> findSeqAndRecordSeqByIsDeletedFalse();

    @Modifying
    @Query("update ShareBoard sb set sb.hit = sb.hit + 1 where sb.seq = :seq")
    int updateHit(@Param("seq") Long seq);
}

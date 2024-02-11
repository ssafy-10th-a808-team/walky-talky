package com.ssafy.backend.shareBoard.repository;

import com.ssafy.backend.shareBoard.domain.ShareBoard;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardMemberMapping;
import com.ssafy.backend.shareBoard.dto.mapping.ShareBoardScrapMapping;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ShareBoardRepository extends JpaRepository<ShareBoard, Long> {
    Page<ShareBoard> findAllByIsDeletedFalse(Pageable pageable);

    Page<ShareBoardMemberMapping> findSeqAndMemberSeqByIsDeletedFalse(Pageable pageable);

    Page<ShareBoardScrapMapping> findSeqAndRecordSeqByIsDeletedFalse(Pageable pageable);

    ShareBoard findBySeqAndIsDeletedFalse(Long seq);

    ShareBoardScrapMapping findRecordSeqBySeqAndIsDeletedFalse(Long seq);

    ShareBoardMemberMapping findMemberSeqBySeqAndIsDeletedFalse(Long seq);

    boolean existsBySeqAndIsDeletedFalse(Long seq);

    int countAllByIsDeletedFalse();

    @Modifying
    @Transactional
    @Query("update ShareBoard sb set sb.hit = sb.hit + 1 where sb.seq = :seq")
    int updateHit(@Param("seq") Long seq);
}

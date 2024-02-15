package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.Dislike;
import com.ssafy.backend.record.dto.mapping.DislikeRecordMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    List<DislikeRecordMapping> findRecordSeqByMemberSeq(Long memberseq);
}

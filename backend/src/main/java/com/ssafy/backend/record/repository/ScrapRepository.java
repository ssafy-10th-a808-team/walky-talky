package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByRecordSeqAndMemberSeq(Long recordSeq, Long memberSeq);

    int countAllByRecordSeq(Long recordSeq);
}

package com.ssafy.backend.scrapRecord.repository;

import com.ssafy.backend.scrapRecord.domain.Scrap;
import com.ssafy.backend.scrapRecord.dto.mapping.RecordSeqMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByRecordSeqAndMemberSeq(Long recordSeq, Long memberSeq);

    int countAllByRecordSeq(Long recordSeq);

    Scrap findByRecordSeqAndMemberSeq(Long recordSeq, Long memberSeq);

    List<RecordSeqMapping> findRecordSeqByMemberSeq(Long memberSeq);

}

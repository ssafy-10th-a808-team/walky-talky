package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findMemberSeqBySeq(Long seq);

}

package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findMemberSeqBySeq(Long seq);

    boolean existsBySeqAndMemberSeqAndIsDeletedFalse(Long seq, Long memberSeq);

    boolean existsBySeqAndIsDeletedTrue(Long seq);

    List<Record> findByMemberSeqAndIsDeletedFalse(Long memberSeq);

    List<Record> findBySeqIn(List<Long> recordSeq);

    List<Record> findByRegionCdAndSeqNotInAndMemberSeqNot(String regionCd, List<Long> recordSeq, Long memberSeq);

    List<Record> findByMemberSeqInAndSeqNotIn(List<Long> memberSeqList, List<Long> recordSeq);

}

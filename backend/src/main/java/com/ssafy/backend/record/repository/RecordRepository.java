package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.Record;
import com.ssafy.backend.record.dto.response.ResponseListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Record findMemberSeqBySeq(Long seq);

    boolean existsBySeqAndMemberSeqAndIsDeletedFalse(Long seq, Long memberSeq);

    @Query("SELECT new com.ssafy.backend.record.dto.response.ResponseListDto(r.seq, r.title, r.starRating, r.comment, r.distance, r.duration) " +
            "FROM Record r WHERE r.memberSeq = :memberSeq AND r.isDeleted = false")
    List<ResponseListDto> findResponseListDtoByMemberSeq(@Param("memberSeq") Long memberSeq);

}

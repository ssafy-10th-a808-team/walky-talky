package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {

    List<RecordDetail> findAllByRecordSeq(Long recordSeq);

    @Modifying
    @Query("UPDATE RecordDetail rd SET rd.url = :url WHERE rd.seq = :recordDetailSeq")
    void updateUrl(@Param("recordDetailSeq") Long recordDetailSeq, @Param("url") String url);

}

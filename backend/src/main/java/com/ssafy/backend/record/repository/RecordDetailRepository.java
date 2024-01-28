package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {

    List<RecordDetail> findAllByRecordSeq(Long recordSeq);

}

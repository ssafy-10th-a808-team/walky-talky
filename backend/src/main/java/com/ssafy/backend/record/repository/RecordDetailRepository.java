package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.mapping.PointsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {

    List<PointsMapping> findAllByRecordSeq(Long recordSeq);

}

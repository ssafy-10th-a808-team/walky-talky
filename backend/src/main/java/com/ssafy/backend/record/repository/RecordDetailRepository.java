package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.RecordDetail;
import com.ssafy.backend.record.dto.PointMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {

    List<RecordDetail> findAllByRecordSeq(Long recordSeq);

    @Query("SELECT r.latitude, r.longitude FROM RecordDetail r " +
            "WHERE r.recordSeq = :recordSeq AND r.url IS NULL AND r.pointComment IS NULL")
    List<PointMapping> findLatitudeAndLongitudeByRecordSeq(@Param("recordSeq") Long recordSeq);

}

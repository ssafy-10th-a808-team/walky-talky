package com.ssafy.backend.record.repository;

import com.ssafy.backend.record.domain.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
}

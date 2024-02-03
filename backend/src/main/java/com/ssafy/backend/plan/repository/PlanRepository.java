package com.ssafy.backend.plan.repository;

import com.ssafy.backend.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByClubSeq(Long clubSeq);
}

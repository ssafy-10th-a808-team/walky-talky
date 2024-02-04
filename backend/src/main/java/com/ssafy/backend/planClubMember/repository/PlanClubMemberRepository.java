package com.ssafy.backend.planClubMember.repository;

import com.ssafy.backend.planClubMember.domain.PlanClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanClubMemberRepository extends JpaRepository<PlanClubMember, Long> {


    List<PlanClubMember> findAllByPlanSeq(Long planSeq);

    void deleteAllByPlanSeq(Long planSeq);

    void deleteByClubMemberSeqAndPlanSeq(Long seq, Long planSeq);
}

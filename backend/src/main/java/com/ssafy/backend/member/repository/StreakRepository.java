package com.ssafy.backend.member.repository;

import com.ssafy.backend.member.domain.Streak;
import com.ssafy.backend.member.dto.mapping.StreakMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {
    List<StreakMapping> findAllByMemberSeq(Long memberSeq);
}

package com.ssafy.backend.member.repository;

import com.ssafy.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByMemberId(String memberId);

    Boolean existsByNickname(String nickname);

    Member findByMemberId(String memberId);
}

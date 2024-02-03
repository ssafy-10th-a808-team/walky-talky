package com.ssafy.backend.member.repository;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.mapping.MemberSeqMapping;
import com.ssafy.backend.member.dto.mapping.NicknameUrlMapping;
import com.ssafy.backend.member.dto.mapping.RegionCdMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByMemberId(String memberId);

    Boolean existsByNickname(String nickname);

    Member findByMemberId(String memberId);

    NicknameUrlMapping findNickNameAndUrlBySeq(Long memberSeq);

    RegionCdMapping findRegionCdBySeq(Long memberSeq);

    @Query("SELECT m.seq FROM Member m WHERE FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', CAST(STR_TO_DATE(m.birth, '%Y%m%d') AS date)) BETWEEN :minAge AND :maxAge AND m.gender = :gender")
    List<MemberSeqMapping> findSeqInAgeAndGender(@Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("gender") String gender);

}

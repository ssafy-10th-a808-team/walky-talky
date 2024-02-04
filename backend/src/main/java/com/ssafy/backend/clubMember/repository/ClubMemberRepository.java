package com.ssafy.backend.clubMember.repository;

import com.ssafy.backend.clubMember.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    boolean existsByClubSeqAndMemberSeqAndRoleIn(Long seq, Long memberSeq, List<String> list);

    List<ClubMember> findAllByClubSeq(Long clubSeq);

    boolean existsByClubSeqAndMemberSeq(Long clubSeq, Long memberSeq);

    ClubMember findByMemberSeqAndClubSeq(Long memberSeq, Long clubSeq);

    boolean existsByClubSeqAndMemberSeqAndRole(Long clubSeq, Long memberSeq, String role);

    List<ClubMember> findAllByClubSeqAndRole(Long clubSeq, String role);

    void deleteByClubSeqAndMemberSeq(Long clubSeq, Long memberSeq);

    List<ClubMember> findAllByClubSeqAndRoleIn(Long clubSeq, List<String> list);

    ClubMember findByMemberSeq(Long myMemberSeq);

    List<ClubMember> findAllByMemberSeq(Long myMemberSeq);

    ClubMember findByClubSeqAndMemberSeq(Long clubSeq, Long myMemberSeq);
}

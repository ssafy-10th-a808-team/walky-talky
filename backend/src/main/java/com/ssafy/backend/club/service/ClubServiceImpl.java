package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    public boolean checkName(RequestCheckNameDto requestCheckNameDto) {
        return clubRepository.existsByName(requestCheckNameDto.getName());
    }

    @Override
    public Club clubCreate(RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) {

        // TODO : Club save
        Club club = requestClubCreateDto.toEntity();
        Club savedClub = clubRepository.save(club);

        // TODO : ClubMember save
        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Member member = memberRepository.findById(memberSeq).orElse(null);

        ClubMember clubMember = ClubMember.builder()
                .club(savedClub)
                .member(member)
                .role("owner")
                .build();

        clubMemberRepository.save(clubMember);

        return savedClub;
    }
}

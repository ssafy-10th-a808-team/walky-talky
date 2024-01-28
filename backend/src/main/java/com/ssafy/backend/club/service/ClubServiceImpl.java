package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.ResponseClubListDto;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public void clubCreate(RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) {

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

    }

    @Override
    public ResponseClubListDto clubList(HttpServletRequest httpServletRequest) {

        ResponseClubListDto responseClubListDto = new ResponseClubListDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");

        Member member = memberRepository.findById(memberSeq).orElse(null);
        List<Club> clubs = clubRepository.findAll();

        responseClubListDto.setMyClubs(new ArrayList<>());
        responseClubListDto.setRecommendClubs(new ArrayList<>());
        responseClubListDto.setAllClubs(new ArrayList<>());

        for (int i = 0; i < clubs.size(); i++) {
            // TODO : clubs.get(i).getSeq()와 MemberSeq를 가지고 club_member 테이블에 role이 owner 또는 member 라는 것이 있으면 responseClubListDto.getMyclubs.add(clubs)하기

            boolean belong = clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubs.get(i).getSeq(), memberSeq, Arrays.asList("owner", "member"));
            if (belong) {
                responseClubListDto.getMyClubs().add(clubs.get(i));
            } else {

                // TODO : clubs의 조건을 보고 성별 나이 동네가 맞다면 추천 목록에 넣어
                // club.is_open_recruite => 1
                // club.now_capacity < club.max_capacity
                // club.gender_type => "M" , "F" , "A"
                // club.young_birth => "2024"
                // club.old_birth => "1996"
                // club.region_cd => "123123?"
                boolean isRecommendClub = true;

                // club 모집 중?
                if (!clubs.get(i).isOpenRecruite())
                    isRecommendClub = false;

                // club 다참?
                if (clubs.get(i).getNowCapacity() == clubs.get(i).getMaxCapacity())
                    isRecommendClub = false;

                // club 성별 모집 조건
                if (!clubs.get(i).getGenderType().equals("A")) {
                    if (!clubs.get(i).getGenderType().equals(member.getGender()))
                        isRecommendClub = false;
                }

                // 나이 조건
                Long memberBirth = Long.parseLong(member.getBirth().substring(0, 4));
                Long clubOldBirth = Long.parseLong(clubs.get(i).getOldBirth());
                Long clubYoungBirth = Long.parseLong(clubs.get(i).getYoungBirth());

                System.out.println("memberBirth = " + memberBirth);
                System.out.println("clubOldBirth = " + clubOldBirth);
                System.out.println("clubYoungBirth = " + clubYoungBirth);


                if (memberBirth <= clubYoungBirth && memberBirth >= clubOldBirth) {

                } else {
                    isRecommendClub = false;
                }

                // TODO : 동네 조건 넣기!!

                if (isRecommendClub) {
                    responseClubListDto.getRecommendClubs().add(clubs.get(i));
                } else {
                    responseClubListDto.getAllClubs().add(clubs.get(i));
                }


            }
        }

        return responseClubListDto;

    }

}

package com.ssafy.backend.clubMember.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.dto.request.RequestClubMemberApplyAcceptDto;
import com.ssafy.backend.clubMember.dto.request.RequestClubMemberApplyDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyAcceptDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyListDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyListDtoMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Override
    public ResponseClubMemberApplyDto clubMemberApply(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyDto responseClubMemberApplyDto = new ResponseClubMemberApplyDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberApplyDto.getClubSeq();

        Member member = memberRepository.findById(memberSeq).orElse(null);
        Club club = clubRepository.findById(clubSeq).orElse(null);

        // 닫혀 있는 소모임입니다.
        if (!club.isOpenRecruite()) {
            responseClubMemberApplyDto.setMessage("닫혀 있는 소모임입니다.");
            return responseClubMemberApplyDto;
        }

        // 성별 조건이 맞지 않습니다.
        if (!club.getGenderType().equals("A")) {
            if (!club.getGenderType().equals(member.getGender())) {
                responseClubMemberApplyDto.setMessage("성별 조건이 맞지 않습니다.");
                return responseClubMemberApplyDto;
            }
        }

        // 정원이 다 찼습니다.
        if (club.getNowCapacity() >= club.getMaxCapacity()) {
            responseClubMemberApplyDto.setMessage("정원이 다 찼습니다.");
            return responseClubMemberApplyDto;
        }


        // 나이 조건이 맞지 않습니다.
        Long memberBirth = Long.parseLong(member.getBirth().substring(0, 4));
        Long oldBirth = Long.parseLong(club.getOldBirth());
        Long youngBirth = Long.parseLong(club.getYoungBirth());

        if (memberBirth < oldBirth || memberBirth > youngBirth) {
            responseClubMemberApplyDto.setMessage("나이 조건이 맞지 않습니다.");
            return responseClubMemberApplyDto;
        }


        // 이미 가입되어 있는 모임 입니다.
        if (clubMemberRepository.existsByClubSeqAndMemberSeq(clubSeq, memberSeq)) {
            responseClubMemberApplyDto.setMessage("이미 가입되어 있는 모임 입니다.");
            return responseClubMemberApplyDto;
        }

        // 바로 가입
        if (club.isAutoRecruite()) {
            ClubMember clubMember = ClubMember.builder()
                    .club(club)
                    .member(member)
                    .role("member")
                    .build();
            clubMemberRepository.save(clubMember);

            // 인원 수 늘리기
            club.setNowCapacity(club.getNowCapacity() + 1);
            clubRepository.save(club);
        }
        // 승낙 대기상태로 가기
        else {
            ClubMember clubMember = ClubMember.builder()
                    .club(club)
                    .member(member)
                    .role("applicant")
                    .build();
            clubMemberRepository.save(clubMember);
        }


        responseClubMemberApplyDto.setMessage("OK");
        return responseClubMemberApplyDto;
    }

    @Override
    public ResponseClubMemberApplyDto clubMemberApplyCancel(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyDto responseClubMemberApplyDto = new ResponseClubMemberApplyDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberApplyDto.getClubSeq();

        Member member = memberRepository.findById(memberSeq).orElse(null);
        Club club = clubRepository.findById(clubSeq).orElse(null);

        ClubMember clubMember = clubMemberRepository.findByMemberSeqAndClubSeq(memberSeq, clubSeq);

        if (clubMember == null) {
            responseClubMemberApplyDto.setMessage("전혀 등록이 없는 모임입니다.");
            return responseClubMemberApplyDto;
        } else {
            if (clubMember.getRole().equals("owner") || clubMember.getRole().equals("member")) {
                responseClubMemberApplyDto.setMessage("owner 혹은 member인 모임입니다.");
                return responseClubMemberApplyDto;
            } else {
                clubMemberRepository.deleteById(clubMember.getSeq());
            }
        }

        responseClubMemberApplyDto.setMessage("OK");
        return responseClubMemberApplyDto;
    }

    @Override
    public ResponseClubMemberApplyListDto clubMemberApplyList(Long clubSeq, HttpServletRequest httpServletRequest) {

        ResponseClubMemberApplyListDto responseClubMemberApplyListDto = new ResponseClubMemberApplyListDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, memberSeq, "owner")) {
            responseClubMemberApplyListDto.setMessage("소모임장이 아니므로 접근 불가능 합니다.");
            return responseClubMemberApplyListDto;
        }

        responseClubMemberApplyListDto.setResponseClubMemberApplyListDtoMembers(new ArrayList<>());

        List<ClubMember> clubMembers = clubMemberRepository.findAllByClubSeqAndRole(clubSeq, "applicant");

        for (ClubMember clubMember : clubMembers) {

            Member tmpMember = clubMember.getMember();

            ResponseClubMemberApplyListDtoMember responseClubMemberApplyListDtoMember = new ResponseClubMemberApplyListDtoMember();

            responseClubMemberApplyListDtoMember.setMemberSeq(tmpMember.getSeq());
            responseClubMemberApplyListDtoMember.setAddress(tmpMember.getAddress());
            responseClubMemberApplyListDtoMember.setUrl(tmpMember.getUrl());
            responseClubMemberApplyListDtoMember.setNickname(tmpMember.getNickname());
            responseClubMemberApplyListDtoMember.setBirth(tmpMember.getBirth());
            responseClubMemberApplyListDtoMember.setIntroduce(tmpMember.getIntroduce());

            responseClubMemberApplyListDto.getResponseClubMemberApplyListDtoMembers().add(responseClubMemberApplyListDtoMember);
        }

        responseClubMemberApplyListDto.setMessage("OK");

        return responseClubMemberApplyListDto;
    }

    @Override
    public ResponseClubMemberApplyAcceptDto clubMemberApplyAccept(RequestClubMemberApplyAcceptDto requestClubMemberApplyAcceptDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyAcceptDto responseClubMemberApplyAcceptDto = new ResponseClubMemberApplyAcceptDto();

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long applicantMemberSeq = requestClubMemberApplyAcceptDto.getMemberSeq();
        Long applicantClubSeq = requestClubMemberApplyAcceptDto.getClubSeq();

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(applicantClubSeq, myMemberSeq, "owner")) {
            responseClubMemberApplyAcceptDto.setMessage("해당 소모임의 owner가 아니므로 수락할 수 없습니다.");
            return responseClubMemberApplyAcceptDto;
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(applicantClubSeq, applicantMemberSeq, "applicant")) {
            responseClubMemberApplyAcceptDto.setMessage("그런 사용자는 현재 applicant 상태가 아닙니다.");
            return responseClubMemberApplyAcceptDto;
        }

        ClubMember clubMember = clubMemberRepository.findByMemberSeqAndClubSeq(applicantMemberSeq, applicantClubSeq);
        clubMember.setRole("member");
        clubMemberRepository.save(clubMember);

        // 인원 수 늘리기
        Club club = clubRepository.findById(applicantClubSeq).orElse(null);
        club.setNowCapacity(club.getNowCapacity() + 1);
        clubRepository.save(club);

        responseClubMemberApplyAcceptDto.setMessage("OK");
        return responseClubMemberApplyAcceptDto;

    }
}

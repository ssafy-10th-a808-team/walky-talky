package com.ssafy.backend.clubMember.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.dto.request.*;
import com.ssafy.backend.clubMember.dto.response.*;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.region.service.RegionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final RegionService regionService;

    @Override
    public ResponseClubMemberApplyDto clubMemberApply(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyDto responseClubMemberApplyDto = new ResponseClubMemberApplyDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberApplyDto.getClubSeq();

        if (clubSeq == null) {
            responseClubMemberApplyDto.setMessage("null data not allowed");
            return responseClubMemberApplyDto;
        }

        Member findedMember = memberRepository.findById(memberSeq).orElse(null);
        Club findedClub = clubRepository.findById(clubSeq).orElse(null);

        // 잘못된 멤버 번호 입니다.
        if (findedMember == null) {
            responseClubMemberApplyDto.setMessage("잘못된 멤버 번호 입니다.");
            return responseClubMemberApplyDto;
        }

        // 잘못된 소모임 번호 입니다.
        if (findedClub == null) {
            responseClubMemberApplyDto.setMessage("잘못된 소모임 번호 입니다.");
            return responseClubMemberApplyDto;
        }

        // 닫혀 있는 소모임입니다.
        if (!findedClub.getIsOpenRecruit()) {
            responseClubMemberApplyDto.setMessage("닫혀 있는 소모임입니다.");
            return responseClubMemberApplyDto;
        }

        // 성별 조건이 맞지 않습니다.
        if (!findedClub.getGenderType().equals("A")) {
            if (!findedClub.getGenderType().equals(findedMember.getGender())) {
                responseClubMemberApplyDto.setMessage("성별 조건이 맞지 않습니다.");
                return responseClubMemberApplyDto;
            }
        }

        // 정원이 다 찼습니다.
        if (findedClub.getNowCapacity() >= findedClub.getMaxCapacity()) {
            responseClubMemberApplyDto.setMessage("정원이 다 찼습니다.");
            return responseClubMemberApplyDto;
        }

        // 나이 조건이 맞지 않습니다.
        Long memberBirth = Long.parseLong(findedMember.getBirth().substring(0, 4));
        Long oldBirth = Long.parseLong(findedClub.getOldBirth());
        Long youngBirth = Long.parseLong(findedClub.getYoungBirth());

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
        if (findedClub.getIsAutoRecruit()) {
            ClubMember clubMember = ClubMember.builder()
                    .club(findedClub)
                    .member(findedMember)
                    .role("member")
                    .build();
            clubMemberRepository.save(clubMember);

            // 인원 수 늘리기
            findedClub.setNowCapacity(findedClub.getNowCapacity() + 1);
            clubRepository.save(findedClub);
        }
        // 승낙 대기상태로 가기
        else {
            ClubMember clubMember = ClubMember.builder()
                    .club(findedClub)
                    .member(findedMember)
                    .role("applicant")
                    .build();
            clubMemberRepository.save(clubMember);
        }


        responseClubMemberApplyDto.setMessage("OK");
        return responseClubMemberApplyDto;
    }

    @Override
    @Transactional
    public ResponseClubMemberApplyDto clubMemberApplyCancel(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyDto responseClubMemberApplyDto = new ResponseClubMemberApplyDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberApplyDto.getClubSeq();

        if (clubSeq == null) {
            responseClubMemberApplyDto.setMessage("null data not allowed");
            return responseClubMemberApplyDto;
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, memberSeq, "applicant")) {
            responseClubMemberApplyDto.setMessage("해당 모임의 신청자가 아닙니다.");
            return responseClubMemberApplyDto;
        }

        clubMemberRepository.deleteByClubSeqAndMemberSeq(clubSeq, memberSeq);

        responseClubMemberApplyDto.setMessage("OK");
        return responseClubMemberApplyDto;
    }

    @Override
    public ResponseClubMemberApplyListDto clubMemberApplyList(Long clubSeq, HttpServletRequest httpServletRequest) {

        ResponseClubMemberApplyListDto responseClubMemberApplyListDto = new ResponseClubMemberApplyListDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");

        if (clubSeq == null) {
            responseClubMemberApplyListDto.setMessage("null data not allowed");
            return responseClubMemberApplyListDto;
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, memberSeq, "owner")) {
            responseClubMemberApplyListDto.setMessage("소모임장이 아니므로 접근 불가능 합니다.");
            return responseClubMemberApplyListDto;
        }

        responseClubMemberApplyListDto.setResponseClubMemberApplyListDtoMembers(new ArrayList<>());

        List<ClubMember> clubMembers = clubMemberRepository.findAllByClubSeqAndRole(clubSeq, "applicant");

        for (ClubMember clubMember : clubMembers) {

            Member tmpMember = clubMember.getMember();

            ResponseClubMemberApplyListDtoMember responseClubMemberApplyListDtoMember = ResponseClubMemberApplyListDtoMember.builder()
                    .memberSeq(tmpMember.getSeq())
                    .address(regionService.findAddress(tmpMember.getRegionCd()))
                    .url(tmpMember.getUrl())
                    .nickname(tmpMember.getNickname())
                    .birth(tmpMember.getBirth())
                    .introduce(tmpMember.getIntroduce())
                    .build();

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

        if (requestClubMemberApplyAcceptDto.getMemberSeq() == null || requestClubMemberApplyAcceptDto.getClubSeq() == null) {
            responseClubMemberApplyAcceptDto.setMessage("request 형식이 올바르지 않음");
            return responseClubMemberApplyAcceptDto;
        }

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

    @Override
    @Transactional
    public ResponseEntity<ResponseClubMemberApplyRejectDto> clubMemberApplyReject(RequestClubMemberApplyRejectDto requestClubMemberApplyRejectDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyRejectDto responseClubMemberApplyRejectDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long applicantMemberSeq = requestClubMemberApplyRejectDto.getMemberSeq();
        Long applicantClubSeq = requestClubMemberApplyRejectDto.getClubSeq();

        if (requestClubMemberApplyRejectDto.getMemberSeq() == null || requestClubMemberApplyRejectDto.getClubSeq() == null) {
            responseClubMemberApplyRejectDto = ResponseClubMemberApplyRejectDto.builder()
                    .message("request 형식이 올바르지 않음")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyRejectDto);
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(applicantClubSeq, myMemberSeq, "owner")) {
            responseClubMemberApplyRejectDto = ResponseClubMemberApplyRejectDto.builder()
                    .message("해당 소모임의 owner가 아니므로 수락할 수 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyRejectDto);
        }

        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(applicantClubSeq, applicantMemberSeq, "applicant")) {
            responseClubMemberApplyRejectDto = ResponseClubMemberApplyRejectDto.builder()
                    .message("그런 사용자는 현재 applicant 상태가 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyRejectDto);
        }

        clubMemberRepository.deleteByClubSeqAndMemberSeq(applicantClubSeq, applicantMemberSeq);

        responseClubMemberApplyRejectDto = ResponseClubMemberApplyRejectDto.builder()
                .message("OK")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberApplyRejectDto);

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseClubMemberExcludeDto> clubMemberExclude(RequestClubMemberExcludeDto requestClubMemberExcludeDto, HttpServletRequest httpServletRequest) {

        ResponseClubMemberExcludeDto responseClubMemberExcludeDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberExcludeDto.getClubSeq();
        Long memberSeq = requestClubMemberExcludeDto.getMemberSeq();

        // 해당 소모임의 주인이 아닙니다.
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, myMemberSeq, "owner")) {
            responseClubMemberExcludeDto = ResponseClubMemberExcludeDto.builder()
                    .message("해당 소모임의 주인이 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberExcludeDto);
        }

        // 해당 소모임의 멤버가 아닙니다.
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, memberSeq, "member")) {
            responseClubMemberExcludeDto = ResponseClubMemberExcludeDto.builder()
                    .message("해당 소모임의 멤버가 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberExcludeDto);
        }

        clubMemberRepository.deleteByClubSeqAndMemberSeq(clubSeq, memberSeq);

        Club findedClub = clubRepository.findById(clubSeq).orElse(null);
        findedClub.setNowCapacity(findedClub.getNowCapacity() - 1);
        clubRepository.save(findedClub);

        responseClubMemberExcludeDto = ResponseClubMemberExcludeDto.builder()
                .message("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberExcludeDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseClubMemberWithdrawDto> clubMemberWithdraw(RequestClubMemberWithdrawDto requestClubMemberWithdrawDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberWithdrawDto responseClubMemberWithdrawDto;

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubMemberWithdrawDto.getClubSeq();

        // 해당 소모임의 멤버가 아닙니다.
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, myMemberSeq, "member")) {
            responseClubMemberWithdrawDto = ResponseClubMemberWithdrawDto.builder()
                    .message("해당 소모임의 멤버가 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberWithdrawDto);
        }

        clubMemberRepository.deleteByClubSeqAndMemberSeq(clubSeq, myMemberSeq);

        Club findedClub = clubRepository.findById(clubSeq).orElse(null);
        findedClub.setNowCapacity(findedClub.getNowCapacity() - 1);
        clubRepository.save(findedClub);

        responseClubMemberWithdrawDto = ResponseClubMemberWithdrawDto.builder()
                .message("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberWithdrawDto);
    }
}

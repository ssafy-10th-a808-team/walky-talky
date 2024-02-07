package com.ssafy.backend.club.service;

import com.ssafy.backend.chat.service.ChatService;
import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.RequestClubDeleteDto;
import com.ssafy.backend.club.dto.request.RequestClubModifyDto;
import com.ssafy.backend.club.dto.response.*;
import com.ssafy.backend.club.repository.ClubRepository;
import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.clubMember.repository.ClubMemberRepository;
import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.region.repository.RegionRepository;
import com.ssafy.backend.region.service.RegionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final S3UploadService s3UploadService;
    private final RegionService regionService;
    private final RegionRepository regionRepository;
    private final ChatService chatService;

    @Override
    public ResponseEntity<ResponseClubCheckNameDto> clubCheckName(RequestClubCheckNameDto requestClubCheckNameDto) {

        ResponseClubCheckNameDto responseClubCheckNameDto;

        if (clubRepository.existsByName(requestClubCheckNameDto.getName())) {
            responseClubCheckNameDto = ResponseClubCheckNameDto.builder()
                    .message("이미 존재하는 소모임 명입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCheckNameDto);
        }

        responseClubCheckNameDto = ResponseClubCheckNameDto.builder()
                .message("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseClubCheckNameDto);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public ResponseEntity<ResponseClubCreateDto> clubCreate(RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException {

        ResponseClubCreateDto responseClubCreateDto;

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");

        Member findedMember = memberRepository.findById(memberSeq).orElse(null);
        if (findedMember == null) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("해당 memberSeq를 가진 회원은 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // 이미 존재하는 소모임 명입니다.
        if (clubRepository.existsByName(requestClubCreateDto.getName())) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("이미 존재하는 소모임 명입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        // 올바르지 않은 지역입니다.
        if (!regionRepository.existsByRegionCd(requestClubCreateDto.getRegionCd())) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("올바르지 않은 지역입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }
        // 올바르지 않은 나이조건입니다.
        String pattern = "^\\d{4}$";
        String inputString1 = requestClubCreateDto.getYoung_birth();
        String inputString2 = requestClubCreateDto.getOld_birth();
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher1 = regex.matcher(inputString1);
        Matcher matcher2 = regex.matcher(inputString2);

        if (!matcher1.matches() || !matcher2.matches()) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("올바르지 않은 나이조건입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // 소모임장의 나이가 소모임 나이 조건에 맞지 않습니다.
        if (Long.parseLong(findedMember.getBirth().substring(0, 4)) < Long.parseLong(requestClubCreateDto.getOld_birth()) ||
                Long.parseLong(findedMember.getBirth().substring(0, 4)) > Long.parseLong(requestClubCreateDto.getYoung_birth())) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("소모임장의 나이가 소모임 나이 조건에 맞지 않습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // 올바르지 않은 성별조건입니다.
        if (!(requestClubCreateDto.getGender_type().equals("A")
                || requestClubCreateDto.getGender_type().equals("M")
                || requestClubCreateDto.getGender_type().equals("F"))) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("올바르지 않은 성별조건입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // 소모임장의 성별이 소모임 성별 조건에 맞지 않습니다.
        if (!requestClubCreateDto.getGender_type().equals("A")) {
            if (!requestClubCreateDto.getGender_type().equals(findedMember.getGender())) {
                responseClubCreateDto = ResponseClubCreateDto.builder()
                        .message("소모임장의 성별이 소모임 성별 조건에 맞지 않습니다.")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);

            }
        }

        // 올바르지 않은 최대 인원입니다.
        if (requestClubCreateDto.getMax_capacity() < 1) {
            responseClubCreateDto = ResponseClubCreateDto.builder()
                    .message("올바르지 않은 최대 인원입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCreateDto);
        }

        // correct club

        // save club
        Club club = requestClubCreateDto.toEntity();
        Club savedClub = clubRepository.save(club);

        if (requestClubCreateDto.getMultipartFile() == null) {
            savedClub.setUrl("https://walkytalky.s3.ap-northeast-2.amazonaws.com/member/37/profile/22b10b5f-2604-4188-8a1d-4408c4bd3791.png");
            clubRepository.save(savedClub);
        }

        // if file data exist
        if (requestClubCreateDto.getMultipartFile() != null && !requestClubCreateDto.getMultipartFile().
                isEmpty()) {
            String tmpUrl = s3UploadService.uploadClubProfileImg(requestClubCreateDto.getMultipartFile(), savedClub.getSeq());
            savedClub.setUrl(tmpUrl);
            clubRepository.save(savedClub);
        }

        //  ClubMember save
        ClubMember clubMember = ClubMember
                .builder()
                .club(savedClub)
                .member(findedMember)
                .role("owner")
                .build();

        clubMemberRepository.save(clubMember);
        chatService.createChatRoom(savedClub.getSeq());

        responseClubCreateDto = ResponseClubCreateDto.builder()
                .message("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseClubCreateDto);
    }

    @Override
    public ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest) {

        ResponseClubListDto responseClubListDto = new ResponseClubListDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Member findedMember = memberRepository.findById(memberSeq).orElse(null);

//        if (findedMember == null) {
//            responseClubListDto = ResponseClubListDto.builder()
//                    .message("해당 memberSeq를 가진 회원은 없습니다.")
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubListDto);
//        }
//
//        if (findedMember.getIsDeleted()) {
//            responseClubListDto = ResponseClubListDto.builder()
//                    .message("탈퇴한 회원 입니다.")
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubListDto);
//        }

        List<Club> allClubs = clubRepository.findAll();

        responseClubListDto.setMyClubs(new ArrayList<>());
        responseClubListDto.setRecommendClubs(new ArrayList<>());
        responseClubListDto.setOtherClubs(new ArrayList<>());

        for (Club curClub : allClubs) {

            // 삭제된 클럽이면 넘기기
            if (curClub.getIsDeleted()) continue;

//            if (!regionRepository.existsByRegionCd(curClub.getRegionCd())) {
//                responseClubListDto = ResponseClubListDto.builder()
//                        .message("올바르지 않은 지역입니다.")
//                        .build();
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubListDto);
//            }

            ResponseClubListDtoClub responseClubListDtoClub = ResponseClubListDtoClub
                    .builder()
                    .seq(curClub.getSeq())
                    .name(curClub.getName())
                    .url(curClub.getUrl())
                    .introduce(curClub.getIntroduce())
                    .address(regionService.findAddress(curClub.getRegionCd()))
                    .youngBirth(curClub.getYoungBirth())
                    .oldBirth(curClub.getOldBirth())
                    .genderType(curClub.getGenderType())
                    .nowCapacity(curClub.getNowCapacity())
                    .maxCapacity(curClub.getMaxCapacity())
                    .isAutoRecruit(curClub.getIsAutoRecruit())
                    .isOpenRecruit(curClub.getIsOpenRecruit())
                    .build();

            boolean belong = clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(curClub.getSeq(), memberSeq, Arrays.asList("owner", "member"));
            if (belong) {
                responseClubListDto.getMyClubs().add(responseClubListDtoClub);
            } else {

                boolean isRecommendClub = true;

                // 모집중이 아니다
                if (!curClub.getIsOpenRecruit()) isRecommendClub = false;

                //  인원이 전부 찼다
                if (curClub.getNowCapacity() >= curClub.getMaxCapacity()) isRecommendClub = false;

                //  나의 성별과 모집 성별이 다르다
                if (!curClub.getGenderType().equals("A")) {
                    if (!curClub.getGenderType().equals(findedMember.getGender())) isRecommendClub = false;
                }

                // 나이 조건
                Long memberBirth = Long.parseLong(findedMember.getBirth().substring(0, 4));
                Long clubOldBirth = Long.parseLong(curClub.getOldBirth());
                Long clubYoungBirth = Long.parseLong(curClub.getYoungBirth());

                if (memberBirth > clubYoungBirth || memberBirth < clubOldBirth) isRecommendClub = false;

                // 동네 조건
                if (!curClub.getRegionCd().equals(findedMember.getRegionCd())) isRecommendClub = false;

                if (isRecommendClub) {
                    responseClubListDto.getRecommendClubs().add(responseClubListDtoClub);
                } else {
                    responseClubListDto.getOtherClubs().add(responseClubListDtoClub);
                }


            }
        }

        responseClubListDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubListDto);

    }

    @Override
    public ResponseEntity<ResponseClubDetailDto> clubDetail(Long clubSeq,HttpServletRequest httpServletRequest) {

        ResponseClubDetailDto responseClubDetailDto = new ResponseClubDetailDto();

        responseClubDetailDto.setResponseClubDetailDtoMembers(new ArrayList<>());

        Club findedClub = clubRepository.findById(clubSeq).orElse(null);

        if (findedClub == null) {
            responseClubDetailDto = ResponseClubDetailDto.builder()
                    .message("해당 clubSeq를 가진 모임은 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
        }

        if (findedClub.getIsDeleted()) {
            responseClubDetailDto = ResponseClubDetailDto.builder()
                    .message("삭제된 모임 입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
        }

//        if (!regionRepository.existsByRegionCd(findedClub.getRegionCd())) {
//            responseClubDetailDto = ResponseClubDetailDto.builder()
//                    .message("올바르지 않은 지역입니다.")
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
//        }

        responseClubDetailDto.setResponseClubDetailDtoClub(
                ResponseClubDetailDtoClub
                        .builder()
                        .seq(findedClub.getSeq())
                        .name(findedClub.getName())
                        .url(findedClub.getUrl())
                        .introduce(findedClub.getIntroduce())
                        .address(regionService.findAddress(findedClub.getRegionCd()))
                        .youngBirth(findedClub.getYoungBirth())
                        .oldBirth(findedClub.getOldBirth())
                        .genderType(findedClub.getGenderType())
                        .nowCapacity(findedClub.getNowCapacity())
                        .maxCapacity(findedClub.getMaxCapacity())
                        .isAutoRecruit(findedClub.getIsAutoRecruit())
                        .isOpenRecruit(findedClub.getIsOpenRecruit())
                        .build());

        // 해당 club의 멤버 모두 찾아 넣기
        List<ClubMember> findedClubMembers = clubMemberRepository.findAllByClubSeq(clubSeq);

//        if (findedClubMembers == null || findedClubMembers.isEmpty()) {
//            responseClubDetailDto = ResponseClubDetailDto.builder()
//                    .message("해당 모임에 회원이 아무도 없습니다.")
//                    .build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
//        }


        for (ClubMember findedClubMember : findedClubMembers) {

//            if (findedClubMember.getRole() == null) {
//                responseClubDetailDto = ResponseClubDetailDto.builder()
//                        .message("역할이 null인 회원 발견!!")
//                        .build();
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
//            }

            if (findedClubMember.getRole().equals("owner") || findedClubMember.getRole().equals("member")) {
                Member tmpMember = findedClubMember.getMember();

                ResponseClubDetailDtoMember responseClubDetailDtoMember = ResponseClubDetailDtoMember
                        .builder()
                        .nickname(tmpMember.getNickname())
                        .url(tmpMember.getUrl())
                        .address(regionService.findAddress(tmpMember.getRegionCd()))
                        .role(findedClubMember
                                .getRole())
                        .build();

                responseClubDetailDto.getResponseClubDetailDtoMembers().add(responseClubDetailDtoMember);
            }
//            else if (findedClubMember.getRole().equals("applicant")) {
//
//            } else {
//                responseClubDetailDto = ResponseClubDetailDto.builder()
//                        .message("역할이 잘못된 회원 발견!!")
//                        .build();
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
//            }
        }

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");

        ClubMember findedClubMember = clubMemberRepository.findByClubSeqAndMemberSeq(clubSeq,myMemberSeq);
        if(findedClubMember == null){
            responseClubDetailDto.setRole("no");
        }else{
            responseClubDetailDto.setRole(findedClubMember.getRole());
        }

        responseClubDetailDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubDetailDto);

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseClubModifyDto> clubModify(
            RequestClubModifyDto requestClubModifyDto,
            HttpServletRequest httpServletRequest) throws IOException {

        ResponseClubModifyDto responseClubModifyDto;

        // TODO : null empty check

        Long myMemberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubModifyDto.getClubSeq();

        Club findedClub = clubRepository.findById(clubSeq).orElse(null);

        // 삭제된 소모임입니다.
        if (findedClub.getIsDeleted()) {
            responseClubModifyDto = ResponseClubModifyDto.builder().message("삭제된 소모임입니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
        }

        // 소모임장이 아닙니다.
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, myMemberSeq, "owner")) {
            responseClubModifyDto = ResponseClubModifyDto.builder().message("소모임장이 아닙니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
        }

        // name이 중복됩니다.
        if (!findedClub.getName().equals(requestClubModifyDto.getName()) && clubRepository.existsByName(requestClubModifyDto.getName())) {
            responseClubModifyDto = ResponseClubModifyDto.builder().message("name이 중복됩니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
        }

        List<ClubMember> findedClubMembers = clubMemberRepository.findAllByClubSeqAndRoleIn(clubSeq, Arrays.asList("owner", "member"));

        // 해당 club의 모든 member들이  new_old_birth <= member <= new_young_birth 인지 검사하기
        // 새로운 나이조건이 기존 멤버들과 맞지 않습니다.
        for (ClubMember clubMember : findedClubMembers) {
            Long tmpMemberBirth = Long.parseLong(clubMember.getMember().getBirth().substring(0, 4));
            Long tmpOldBirth = Long.parseLong(requestClubModifyDto.getOld_birth());
            Long tmpYongBirth = Long.parseLong(requestClubModifyDto.getYoung_birth());
            if (tmpMemberBirth < tmpOldBirth || tmpMemberBirth > tmpYongBirth) {
                responseClubModifyDto = ResponseClubModifyDto.builder().message("새로운 나이조건이 기존 멤버들과 맞지 않습니다.").build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
            }
        }

        // 해당 club의 모든 member들이 new_gender_type을 만족하는지 검사하기
        // 새로운 성별조건이 기존 멤버들과 맞지 않습니다.
        for (ClubMember clubMember : findedClubMembers) {
            if (requestClubModifyDto.getGender_type().equals("A")) {

            } else {
                if (requestClubModifyDto.getGender_type().equals(clubMember.getMember().getGender())) {

                } else {
                    responseClubModifyDto = ResponseClubModifyDto.builder().message("새로운 성별조건이 기존 멤버들과 맞지 않습니다.").build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
                }
            }
        }

        // 해당 club의 현재 now_capacity <= new_max_capacity 인지 검사하기
        // 새로운 최대 인원 조건이 현재 인원보다 큽니다.
        if (findedClub.getNowCapacity() > requestClubModifyDto.getMax_capacity()) {
            responseClubModifyDto = ResponseClubModifyDto.builder().message("새로운 최대 인원 조건이 현재 인원보다 큽니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
        }

        // DB에 club 값 변경하기

        // if file data exist
        if (requestClubModifyDto.getMultipartFile() != null && !requestClubModifyDto.getMultipartFile().isEmpty()) {
            // 현재 사진이 있는 경우
            if (findedClub.getUrl() != null)
                s3UploadService.deleteImg(findedClub.getUrl());
            String tmpUrl = s3UploadService.uploadClubProfileImg(requestClubModifyDto.getMultipartFile(), findedClub.getSeq());
            findedClub.setUrl(tmpUrl);
        }

        findedClub.setName(requestClubModifyDto.getName());
        if (requestClubModifyDto.getIntroduce() != null)
            findedClub.setIntroduce(requestClubModifyDto.getIntroduce());
        findedClub.setRegionCd(requestClubModifyDto.getRegionCd());
        findedClub.setYoungBirth(requestClubModifyDto.getYoung_birth());
        findedClub.setOldBirth(requestClubModifyDto.getOld_birth());
        findedClub.setGenderType(requestClubModifyDto.getGender_type());
        findedClub.setMaxCapacity(requestClubModifyDto.getMax_capacity());
        findedClub.setIsAutoRecruit(requestClubModifyDto.getIs_auto_recruit());
        findedClub.setIsOpenRecruit(requestClubModifyDto.getIs_open_recruit());

        clubRepository.save(findedClub);

        responseClubModifyDto = ResponseClubModifyDto.builder()
                .message("OK")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseClubModifyDto);

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseClubDeleteDto> clubDelete(RequestClubDeleteDto requestClubDeleteDto, HttpServletRequest httpServletRequest) {

        ResponseClubDeleteDto responseClubDeleteDto;

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Long clubSeq = requestClubDeleteDto.getClubSeq();

        // requestClubDeleteDto의 get 했을 때 null이면 쫓아내기
        if (clubSeq == null) {
            responseClubDeleteDto = ResponseClubDeleteDto.builder()
                    .message("ClubSeq가 null입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDeleteDto);
        }

        // 없는 소모임 번호면 쫓아내기
        if (!clubRepository.existsById(clubSeq)) {
            responseClubDeleteDto = ResponseClubDeleteDto.builder()
                    .message("없는 소모임 번호 입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDeleteDto);
        }

        // 이미 삭제된 소모임이면 쫓아내기
        if (clubRepository.findById(clubSeq).orElse(null).getIsDeleted()) {
            responseClubDeleteDto = ResponseClubDeleteDto.builder()
                    .message("이미 삭제된 소모임입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDeleteDto);
        }

        // club의 owner가 아니면 쫓아내기
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(clubSeq, memberSeq, "owner")) {
            responseClubDeleteDto = ResponseClubDeleteDto.builder()
                    .message("해당 소모임의 owner가 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDeleteDto);
        }

        // club의 혼자가 아니면 쫓아내기
        if (clubMemberRepository.findAllByClubSeqAndRoleIn(clubSeq, Arrays.asList("owner", "member")).size() != 1) {
            responseClubDeleteDto = ResponseClubDeleteDto.builder()
                    .message("해당 소모임에 혼자가 아닙니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDeleteDto);
        }

        clubMemberRepository.deleteByClubSeqAndMemberSeq(clubSeq, memberSeq);

        Club club = clubRepository.findById(clubSeq).orElse(null);
        club.setIsDeleted(true);
        clubRepository.save(club);

        responseClubDeleteDto = ResponseClubDeleteDto.builder()
                .message("OK")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseClubDeleteDto);
    }
}

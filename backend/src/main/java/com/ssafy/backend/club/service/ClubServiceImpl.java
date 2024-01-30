package com.ssafy.backend.club.service;

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

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final S3UploadService s3UploadService;
    private final RegionService regionService;


    @Override
    public ResponseEntity<ResponseClubCheckNameDto> clubCheckName(RequestClubCheckNameDto requestClubCheckNameDto) {

        ResponseClubCheckNameDto responseClubCheckNameDto = new ResponseClubCheckNameDto();

        if (requestClubCheckNameDto.getName() == null || requestClubCheckNameDto.getName().isEmpty()) {
            responseClubCheckNameDto.setMessage("올바르지 않은 입력입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCheckNameDto);
        }

        if (clubRepository.existsByName(requestClubCheckNameDto.getName())) {
            responseClubCheckNameDto.setMessage("이미 존재하는 소모임 명입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubCheckNameDto);
        }

        responseClubCheckNameDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubCheckNameDto);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public ResponseEntity<ResponseClubCreateDto> clubCreate(MultipartFile multipartFile, RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException {

        ResponseClubCreateDto responseClubCreateDto = new ResponseClubCreateDto();

        // save club
        Club club = requestClubCreateDto.toEntity();
        Club savedClub = clubRepository.save(club);

        // file data exist
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String tmpUrl = s3UploadService.uploadClubProfileImg(multipartFile, savedClub.getSeq());
            savedClub.setUrl(tmpUrl);
            clubRepository.save(savedClub);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        //  ClubMember save
        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Member findedMember = memberRepository.findById(memberSeq).orElse(null);

        ClubMember clubMember = ClubMember.builder().club(savedClub).member(findedMember).role("owner").build();

        clubMemberRepository.save(clubMember);

        responseClubCreateDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubCreateDto);
    }

    @Override
    public ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest) {

        ResponseClubListDto responseClubListDto = new ResponseClubListDto();

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");
        Member findedMember = memberRepository.findById(memberSeq).orElse(null);
        List<Club> allClubs = clubRepository.findAll();

        responseClubListDto.setMyClubs(new ArrayList<>());
        responseClubListDto.setRecommendClubs(new ArrayList<>());
        responseClubListDto.setOtherClubs(new ArrayList<>());

        for (Club curClub : allClubs) {

            // 삭제된 클럽이면 넘기기
            if (curClub.getIsDeleted()) continue;

            ResponseClubListDtoClub responseClubListDtoClub = ResponseClubListDtoClub.builder().seq(curClub.getSeq()).name(curClub.getName()).url(curClub.getUrl()).introduce(curClub.getIntroduce()).address(regionService.findAddress(curClub.getRegionCd())).youngBirth(curClub.getYoungBirth()).oldBirth(curClub.getOldBirth()).genderType(curClub.getGenderType()).nowCapacity(curClub.getNowCapacity()).maxCapacity(curClub.getMaxCapacity()).isAutoRecruit(curClub.getIsAutoRecruit()).isOpenRecruit(curClub.getIsOpenRecruit()).build();

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
    public ResponseEntity<ResponseClubDetailDto> clubDetail(Long clubSeq) {

        ResponseClubDetailDto responseClubDetailDto = new ResponseClubDetailDto();
        responseClubDetailDto.setResponseClubDetailDtoMembers(new ArrayList<>());

        // clubSeq의 club을 찾아서 넣기
        Club findedClub = clubRepository.findById(clubSeq).orElse(null);

        if (findedClub == null || findedClub.getIsDeleted()) {
            responseClubDetailDto.setMessage("잘못된 소모임번호 입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
        }

        responseClubDetailDto.setResponseClubDetailDtoClub(ResponseClubDetailDtoClub.builder().seq(findedClub.getSeq()).name(findedClub.getName()).url(findedClub.getUrl()).introduce(findedClub.getIntroduce()).address(regionService.findAddress(findedClub.getRegionCd())).youngBirth(findedClub.getYoungBirth()).oldBirth(findedClub.getOldBirth()).genderType(findedClub.getGenderType()).nowCapacity(findedClub.getNowCapacity()).maxCapacity(findedClub.getMaxCapacity()).isAutoRecruit(findedClub.getIsAutoRecruit()).isOpenRecruit(findedClub.getIsOpenRecruit()).build());

        // 해당 club의 멤버 모두 찾아 넣기
        List<ClubMember> findedClubMembers = clubMemberRepository.findAllByClubSeq(clubSeq);

        for (ClubMember findedClubMember : findedClubMembers) {
            if (findedClubMember.getRole().equals("owner") || findedClubMember.getRole().equals("member")) {
                Member tmpMember = findedClubMember.getMember();

                ResponseClubDetailDtoMember responseClubDetailDtoMember = ResponseClubDetailDtoMember.builder().nickname(tmpMember.getNickname()).url(tmpMember.getUrl()).address(regionService.findAddress(tmpMember.getRegionCd())).role(findedClubMember.getRole()).build();


                responseClubDetailDto.getResponseClubDetailDtoMembers().add(responseClubDetailDtoMember);
            }
        }

        responseClubDetailDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubDetailDto);

    }

    @Override
    public ResponseEntity<ResponseClubModifyDto> clubModify(
            MultipartFile multipartFile,
            RequestClubModifyDto requestClubModifyDto,
            HttpServletRequest httpServletRequest) throws IOException {

        ResponseClubModifyDto responseClubModifyDto;

        Long memberSeq = (Long) httpServletRequest.getAttribute("seq");

        // TODO : 삭제된 소모임이면 쫓아내기

        // TODO : requestClubModifyDto.get 했을 때 null 이나 empty가 있으면 쫓아내기

        // memberSeq가 club의 owner가 아니면 쫓아내기
        if (!clubMemberRepository.existsByClubSeqAndMemberSeqAndRole(requestClubModifyDto.getClubSeq(), memberSeq, "owner")) {
            responseClubModifyDto = ResponseClubModifyDto.builder().message("소모임 장이 아닙니다.").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubModifyDto);
        }

        // TODO : name이 다른 club과 중복되는지 검사하기

        // TODO : 해당 club의 모든 member들이  new_old_birth <= member <= new_young_birth 인지 검사하기

        // TODO : 해당 club의 모든 member들이 new_gender_type을 만족하는지 검사하기

        // TODO : 해당 club의 현재 now_capacity <= new_max_capacity 인지 검사하기

        // DB에 club 값 변경하기
        Club curClub = clubRepository.findById(requestClubModifyDto.getClubSeq()).orElse(null);

        // request 사진이 있을 경우
        if (multipartFile != null && !multipartFile.isEmpty()) {
            // 현재 사진이 있는 경우
            if (curClub.getUrl() != null)
                s3UploadService.deleteImg(curClub.getUrl());
            String tmpUrl = s3UploadService.uploadClubProfileImg(multipartFile, curClub.getSeq());
            curClub.setUrl(tmpUrl);
        }

        curClub.setName(requestClubModifyDto.getName());
        curClub.setIntroduce(requestClubModifyDto.getIntroduce());
        curClub.setRegionCd(requestClubModifyDto.getRegionCd());
        curClub.setYoungBirth(requestClubModifyDto.getYoung_birth());
        curClub.setOldBirth(requestClubModifyDto.getOld_birth());
        curClub.setGenderType(requestClubModifyDto.getGender_type());
        curClub.setMaxCapacity(requestClubModifyDto.getMax_capacity());
        curClub.setIsAutoRecruit(requestClubModifyDto.getIs_auto_recruit());
        curClub.setIsOpenRecruit(requestClubModifyDto.getIs_open_recruit());

        clubRepository.save(curClub);

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

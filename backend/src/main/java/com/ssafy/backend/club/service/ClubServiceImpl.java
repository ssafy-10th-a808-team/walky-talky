package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
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

        ClubMember clubMember = ClubMember.builder()
                .club(savedClub)
                .member(findedMember)
                .role("owner")
                .build();

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
            if (curClub.isDeleted())
                continue;

            ResponseClubListDtoClub responseClubListDtoClub =
                    ResponseClubListDtoClub.builder()
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
                            .isAutoRecruit(curClub.isAutoRecruit())
                            .isOpenRecruit(curClub.isOpenRecruit())
                            .build();

            boolean belong = clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(curClub.getSeq(), memberSeq, Arrays.asList("owner", "member"));
            if (belong) {
                responseClubListDto.getMyClubs().add(responseClubListDtoClub);
            } else {

                boolean isRecommendClub = true;

                // 모집중이 아니다
                if (!curClub.isOpenRecruit())
                    isRecommendClub = false;

                //  인원이 전부 찼다
                if (curClub.getNowCapacity() >= curClub.getMaxCapacity())
                    isRecommendClub = false;

                //  나의 성별과 모집 성별이 다르다
                if (!curClub.getGenderType().equals("A")) {
                    if (!curClub.getGenderType().equals(findedMember.getGender()))
                        isRecommendClub = false;
                }

                // 나이 조건
                Long memberBirth = Long.parseLong(findedMember.getBirth().substring(0, 4));
                Long clubOldBirth = Long.parseLong(curClub.getOldBirth());
                Long clubYoungBirth = Long.parseLong(curClub.getYoungBirth());

                if (memberBirth > clubYoungBirth || memberBirth < clubOldBirth)
                    isRecommendClub = false;

                // 동네 조건
                if (!curClub.getRegionCd().equals(findedMember.getRegionCd()))
                    isRecommendClub = false;

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

        if (findedClub == null || findedClub.isDeleted()) {
            responseClubDetailDto.setMessage("잘못된 소모임번호 입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubDetailDto);
        }

        responseClubDetailDto.setResponseClubDetailDtoClub(
                ResponseClubDetailDtoClub.builder()
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
                        .isAutoRecruit(findedClub.isAutoRecruit())
                        .isOpenRecruit(findedClub.isOpenRecruit())
                        .build()
        );

        // 해당 club의 멤버 모두 찾아 넣기
        List<ClubMember> findedClubMembers = clubMemberRepository.findAllByClubSeq(clubSeq);

        for (ClubMember findedClubMember : findedClubMembers) {
            if (findedClubMember.getRole().equals("owner") || findedClubMember.getRole().equals("member")) {
                Member tmpMember = findedClubMember.getMember();

                ResponseClubDetailDtoMember responseClubDetailDtoMember = ResponseClubDetailDtoMember.builder()
                        .nickname(tmpMember.getNickname())
                        .url(tmpMember.getUrl())
                        .address(regionService.findAddress(tmpMember.getRegionCd()))
                        .role(findedClubMember.getRole())
                        .build();


                responseClubDetailDto.getResponseClubDetailDtoMembers().add(responseClubDetailDtoMember);
            }
        }

        responseClubDetailDto.setMessage("OK");
        return ResponseEntity.status(HttpStatus.OK).body(responseClubDetailDto);

    }


}

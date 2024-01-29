package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.response.ResponseClubListDto;
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
    public boolean checkName(RequestCheckNameDto requestCheckNameDto) {
        return clubRepository.existsByName(requestCheckNameDto.getName());
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void clubCreate(MultipartFile multipartFile, RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException {

        //  Club save
        Club club = requestClubCreateDto.toEntity();

        // find address
        club.setAddress(regionService.findAddress(club.getRegionCd()));

        Club savedClub = clubRepository.save(club);

        String tmpUrl = s3UploadService.uploadClubProfileImg(multipartFile, savedClub.getSeq());
        savedClub.setUrl(tmpUrl);

        clubRepository.save(savedClub);

        //  ClubMember save
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
            //  clubs.get(i).getSeq()와 MemberSeq를 가지고 club_member 테이블에 role이 owner 또는 member 라는 것이 있으면 responseClubListDto.getMyClubs().add(clubs)하기

            boolean belong = clubMemberRepository.existsByClubSeqAndMemberSeqAndRoleIn(clubs.get(i).getSeq(), memberSeq, Arrays.asList("owner", "member"));
            if (belong) {
                responseClubListDto.getMyClubs().add(clubs.get(i));
            } else {

                boolean isRecommendClub = true;

                // 모집중이 아니다
                if (!clubs.get(i).isOpenRecruite())
                    isRecommendClub = false;

                //  인원이 전부 찼다
                if (clubs.get(i).getNowCapacity() == clubs.get(i).getMaxCapacity())
                    isRecommendClub = false;

                //  나의 성별과 모집 성별이 다르다
                if (!clubs.get(i).getGenderType().equals("A")) {
                    if (!clubs.get(i).getGenderType().equals(member.getGender()))
                        isRecommendClub = false;
                }

                // 나이 조건
                Long memberBirth = Long.parseLong(member.getBirth().substring(0, 4));
                Long clubOldBirth = Long.parseLong(clubs.get(i).getOldBirth());
                Long clubYoungBirth = Long.parseLong(clubs.get(i).getYoungBirth());

                if (memberBirth > clubYoungBirth || memberBirth < clubOldBirth)
                    isRecommendClub = false;

                // 동네 조건
                if (!clubs.get(i).getRegionCd().equals(member.getRegionCd()))
                    isRecommendClub = false;

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

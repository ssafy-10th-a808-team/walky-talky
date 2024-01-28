package com.ssafy.backend.member.service;

import com.ssafy.backend.common.service.S3UploadService;
import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import com.ssafy.backend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    private final RedisDao redisDao;

    private final S3UploadService s3UploadService;

    private static final long atkExp = 900000L; // 15분
    private static final long rtkExp = 604800000L; // 일주일

    @Override
    public boolean checkId(RequestCheckIdDto requestCheckIdDto) {
        return memberRepository.existsByMemberId(requestCheckIdDto.getMemberId());
    }

    @Override
    public boolean checkNickname(RequestCheckNicknameDto requestCheckNicknameDto) {
        return memberRepository.existsByNickname(requestCheckNicknameDto.getNickname());
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void localSignup(MultipartFile multipartFile, RequestLocalSignupDto requestLocalSignupDto) throws IOException {

        Member member = requestLocalSignupDto.toEntity();

        Member savedMember = memberRepository.save(member);

        String tmpUrl = s3UploadService.uploadMemberProfileImg(multipartFile, savedMember.getSeq());

        savedMember.setUrl(tmpUrl);

        memberRepository.save(savedMember);

    }

    @Override
    public Map<String, String> localLogin(RequestLocalLoginDto loginDto) {
        Member member = memberRepository.findByMemberId(loginDto.getMemberId());
        Map<String, String> returnMap = new HashMap<>();

        if (member == null) { // 아이디에 해당하는 회원 없을때
            returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
            return returnMap;
        } else {
            if (member.isDeleted()) { // 탈퇴한 회원일때
                returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return returnMap;
            }
            if (!loginDto.getPassword().equals(member.getPassword())) { // 비번 틀렸을때
                returnMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return returnMap;
            }
        }
        // jwt
        Long seq = member.getSeq();
        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        return returnMap;
    }

    @Override
    public void logout(Long seq) {
        redisDao.deleteFromRedis("atk:" + seq);
        redisDao.deleteFromRedis("rtk:" + seq);
    }

    @Override
    public Map<String, String> reissue(Long seq) {
        Map<String, String> returnMap = new HashMap<>();

        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        return returnMap;
    }

}

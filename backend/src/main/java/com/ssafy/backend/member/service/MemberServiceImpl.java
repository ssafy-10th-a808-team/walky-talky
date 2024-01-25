package com.ssafy.backend.member.service;

import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import com.ssafy.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    private final RedisDao redisDao;

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
    public Member localSignup(RequestLocalSignupDto requestLocalSignupDto) {
        Member member = requestLocalSignupDto.toEntity();
        return memberRepository.save(member);
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
        String memberId = member.getMemberId();
        String atk = jwtProvider.createAccessToken(memberId, atkExp);
        String rtk = jwtProvider.createRefreshToken(memberId, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + memberId, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + memberId, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        return returnMap;
    }

    @Override
    public void logout(String memberId) {
        redisDao.deleteFromRedis("atk:" + memberId);
        redisDao.deleteFromRedis("rtk:" + memberId);
    }

    public Map<String, String> reissue(String memberId){
        Map<String, String> returnMap = new HashMap<>();

        String atk = jwtProvider.createAccessToken(memberId, atkExp);
        String rtk = jwtProvider.createRefreshToken(memberId, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + memberId, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + memberId, rtk, Duration.ofMillis(rtkExp));

        returnMap.put("atk", atk);
        returnMap.put("rtk", rtk);

        return returnMap;
    }

}

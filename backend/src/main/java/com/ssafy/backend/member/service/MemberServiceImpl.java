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

    private static final long atkExp = 900000L;
    private static final long rtkExp = 604800000L;

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
    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Member member = memberRepository.findByMemberId(loginDto.getMemberId());

        if (member == null) { // 아이디에 해당하는 회원 없을때
            resultMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
            return resultMap;
        } else {
            if (member.isDeleted()) { // 탈퇴한 회원일때
                resultMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return resultMap;
            }
            if (!loginDto.getPassword().equals(member.getPassword())) { // 비번 틀렸을때
                resultMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
                return resultMap;
            }
        }

        String seq = member.getSeq().toString();
        // jwt token
        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createAccessToken(seq, rtkExp);

        // redis 저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        resultMap.put("message", "OK");

        return resultMap;
    }


}

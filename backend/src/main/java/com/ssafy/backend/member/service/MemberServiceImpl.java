package com.ssafy.backend.member.service;

import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
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
    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    public boolean localLogin(RequestLocalLoginDto loginDto) {
        Member member = memberRepository.findByMemberId(loginDto.getMemberId());

        if (member == null) { // 아이디에 해당하는 회원 없을때
            return false;
        } else {
            if (member.isDeleted()) { // 탈퇴한 회원일때
                return false;
            }
            if (!loginDto.getPassword().equals(member.getPassword())) { // 비번 틀렸을때
                return false;
            }
        }

        // jwt
        String seq = member.getSeq().toString();
        String atk = jwtProvider.createAccessToken(seq, atkExp);
        String rtk = jwtProvider.createRefreshToken(seq, rtkExp);

        // redis에 jwt저장
        redisDao.saveToRedis("atk:" + seq, atk, Duration.ofMillis(atkExp));
        redisDao.saveToRedis("rtk:" + seq, rtk, Duration.ofMillis(rtkExp));

        return true;
    }

}

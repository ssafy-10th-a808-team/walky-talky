package com.ssafy.backend.oauth.service;

import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthInfoDto;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {
    //    private static final long atkExp = 900000L; // 15분
    private static final long atkExp = 604800000L; // 일주일
    private static final long rtkExp = 604800000L; // 일주일

    private final RedisDao redisDao;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RequestOauthService requestOauthService;

    @Override
    public Map<String, Object> login(String code) {
        ResponseOauthTokenDto responseOauthTokenDto = requestOauthService.requestAccessToken(code);
        ResponseOauthInfoDto responseOauthInfoDto = requestOauthService.requestUserInfo(responseOauthTokenDto);
        Map<String, Object> returnMap = new HashMap<>();
        Member findMember = memberRepository.findByMemberId(responseOauthInfoDto.getKakaoAccount().getEmail());

        if (findMember == null) {
            /**
             * 회원가입
             *
             * Map에 email, nickname, imgUrl 담아서 응답
             * 이후 local-signup으로 요청
             */
            returnMap.put("id", responseOauthInfoDto.getKakaoAccount().getEmail());
            returnMap.put("nickname", responseOauthInfoDto.getKakaoAccount().getProfile().getNickname());
            returnMap.put("profileImage", responseOauthInfoDto.getKakaoAccount().getProfile().getProfileImageUrl());

            return returnMap;
        } else {
            /**
             * 로그인
             *
             * atk, rtk 발급
             */
            // jwt
            Long seq = findMember.getSeq();
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
}
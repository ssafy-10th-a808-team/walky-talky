package com.ssafy.backend.oauth.service;

import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.member.repository.MemberRepository;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthInfoDto;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RequestOauthService requestOauthService;

    @Override
    public Map<String, Object> login(String code) {
        ResponseOauthTokenDto responseOauthTokenDto = requestOauthService.requestAccessToken(code);
        ResponseOauthInfoDto responseOauthInfoDto = requestOauthService.requestUserInfo(responseOauthTokenDto);
        if (memberRepository.existsByMemberId(responseOauthInfoDto.getEmail())) {
            /**
             * 로그인
             *
             * atk, rtk 발급
             */
        } else {
            /**
             * 회원가입
             *
             * Map에 email, nickname, imgUrl 담아서 응답
             * 이후 local-signup으로 요청
             */
        }
        return null;
    }
}
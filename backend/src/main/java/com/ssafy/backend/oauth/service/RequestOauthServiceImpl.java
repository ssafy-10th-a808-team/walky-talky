package com.ssafy.backend.oauth.service;

import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthInfoDto;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RequestOauthServiceImpl implements RequestOauthService {

    private final RestTemplate restTemplate;

    @Override
    public ResponseOauthTokenDto requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // 수정할 것
        params.add("grant_type", "authorization_code");
        params.add("client_id", "authorization_code");
        params.add("redirect_uri", "authorization_code");
        params.add("code", "code");
        params.add("client_secret", "client_secret");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(
                "https://kauth.kakao.com/oauth/token",
                kakaoTokenRequest,
                ResponseOauthTokenDto.class
        );
    }

    @Override
    public ResponseOauthInfoDto requestUserInfo(ResponseOauthTokenDto accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(
                "https://kapi.kakao.com/v2/user/me",
                request,
                ResponseOauthInfoDto.class
        );
    }
}

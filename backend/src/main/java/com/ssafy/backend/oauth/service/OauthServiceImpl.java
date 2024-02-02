package com.ssafy.backend.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthServiceImpl implements OauthService {


    @Override
    public ResponseOauthTokenDto getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "authorization_code");
        params.add("redirect_uri", "authorization_code");
        params.add("code", "code");
        params.add("client_secret", "client_secret");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        ResponseOauthTokenDto responseOauthTokenDto;
        try {
            responseOauthTokenDto = om.readValue(accessTokenResponse.getBody(), ResponseOauthTokenDto.class);
        } catch (JsonProcessingException e) {
            throw new WTException("회원가입에 실패하였습니다.");
        }

        return responseOauthTokenDto;

    }

    @Override
    public void saveUser(ResponseOauthTokenDto accessToken) {

    }
}
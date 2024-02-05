package com.ssafy.backend.oauth.service;

import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthInfoDto;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RequestOauthServiceImpl implements RequestOauthService {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.request-token-uri}")
    private String requestTokenUri;

    @Value("${oauth.kakao.request-user-info-uri}")
    private String requestUserInfoUri;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public ResponseOauthTokenDto requestAccessToken(String code) {
        String url = requestTokenUri;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                ResponseOauthTokenDto.class).getBody();
    }

    @Override
    public ResponseOauthInfoDto requestUserInfo(ResponseOauthTokenDto accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken.getAccess_token());

        String requestBody = "property_keys=" + "[\"kakao_account.profile\",\"kakao_account.email\"]";

        HttpEntity<String > request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(
                requestUserInfoUri,
                HttpMethod.POST,
                request,
                ResponseOauthInfoDto.class).getBody();
    }
}

package com.ssafy.backend.oauth.service;

import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;

public interface OauthService {
    ResponseOauthTokenDto getAccessToken(String code);

    void saveUser(ResponseOauthTokenDto accessToken);
}

package com.ssafy.backend.oauth.service;

import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthInfoDto;
import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;

public interface RequestOauthService {

    ResponseOauthTokenDto requestAccessToken(String code);
    ResponseOauthInfoDto requestUserInfo(ResponseOauthTokenDto accessToken);
}

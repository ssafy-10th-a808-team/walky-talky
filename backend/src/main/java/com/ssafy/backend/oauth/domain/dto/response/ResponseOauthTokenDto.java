package com.ssafy.backend.oauth.domain.dto.response;

import lombok.Data;

@Data
public class ResponseOauthTokenDto {
    String token_type;
    String access_token;
    String expires_in;
    String refresh_token;
    String refresh_token_expires_in;
}

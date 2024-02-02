package com.ssafy.backend.oauth.domain.dto.response;

import lombok.Data;

@Data
public class ResponseOauthTokenDto {
    private String token_type;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
}

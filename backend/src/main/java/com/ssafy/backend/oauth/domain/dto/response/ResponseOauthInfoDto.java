package com.ssafy.backend.oauth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseOauthInfoDto {

    private KakaoAccount kakao_account;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class KakaoProfile {
        private String nickname;
        private String thumbnail_image_url;
    }

    public String getEmail() {
        return kakao_account.email;
    }

    public String getNickname() {
        return kakao_account.profile.nickname;
    }

    public String getImg() {
        return kakao_account.profile.thumbnail_image_url;
    }
}

package com.ssafy.backend.oauth.controller;

import com.ssafy.backend.oauth.domain.dto.response.ResponseOauthTokenDto;
import com.ssafy.backend.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/oauth/token")
    public ResponseEntity<?> getLogin(@RequestParam("code") String code) {
        ResponseOauthTokenDto accessToken = oauthService.getAccessToken(code);

        oauthService.saveUser(accessToken);
        return null;
    }
}

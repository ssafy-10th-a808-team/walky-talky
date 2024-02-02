package com.ssafy.backend.oauth.controller;

import com.ssafy.backend.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/oauth/kakao")
    public ResponseEntity<?> login(@RequestParam("code") String code) {
        Map<String, Object> resultMap = oauthService.login(code);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resultMap);
    }
}

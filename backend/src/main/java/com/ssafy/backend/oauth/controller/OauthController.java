package com.ssafy.backend.oauth.controller;

import com.ssafy.backend.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> login(@RequestParam("code") String code) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = oauthService.login(code);

        if (data.containsKey("isDeleted")) {
            resultMap.put("message", "탈퇴한 회원입니다.");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }

        // 로그인의 경우
        if (data.containsKey("atk")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("atk", (String) data.get("atk"));
            data.remove("atk");
            headers.add("rtk", (String) data.get("rtk"));
            data.remove("rtk");

            resultMap.put("data", data);
            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resultMap);
        }
        // 회원가입인 경우
        else {
            return ResponseEntity.status(HttpStatus.CREATED).body(data);
        }
    }
}

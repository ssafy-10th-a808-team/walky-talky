package com.ssafy.backend.member.controller;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestMemberCheckIdDto;
import com.ssafy.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check/{memberId}")
    public ResponseEntity<Map<String, Object>> getMemberCheckMemberId(@PathVariable String memberId) {

        System.out.println("memberId = " + memberId);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        Member member = memberService.findByMemberId(memberId);
        if (member == null) {
            resultMap.put("message", "OK");
            status = HttpStatus.OK;
        } else {
            resultMap.put("message", "중복된 아이디입니다.");
            status = HttpStatus.BAD_REQUEST;
        }

        System.out.println("status = " + status);

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/check-id")
    public ResponseEntity<Map<String, Object>> postMemberCheckId(@RequestBody RequestMemberCheckIdDto requestMemberCheckIdDto) {
        System.out.println("requestMemberCheckIdDto.getMemberId() = " + requestMemberCheckIdDto.getMemberId());

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        Member member = memberService.findByMemberId(requestMemberCheckIdDto.getMemberId());
        if (member == null) {
            resultMap.put("message", "OK");
            status = HttpStatus.OK;
        } else {
            resultMap.put("message", "중복된 아이디입니다.");
            status = HttpStatus.BAD_REQUEST;
        }

        System.out.println("status = " + status);

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/local-login")
    public ResponseEntity<?> localLogin(@RequestBody RequestLocalLoginDto loginDto) {
        Map<String, String> resultMap = memberService.localLogin(loginDto);

        try {
            if (resultMap.containsKey("atk")) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + resultMap.get("atk"));
                resultMap.remove("atk");

                return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resultMap);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

        } catch (Exception e) {
            resultMap.put("message", "아이디 혹은 비밀번호를 확인해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, String> resultMap = new HashMap<>();


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

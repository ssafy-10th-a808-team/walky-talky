package com.ssafy.backend.member.controller;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.response.ResponseCheckIdDto;
import com.ssafy.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/check-id")
    public ResponseEntity<ResponseCheckIdDto> CheckId(@RequestBody RequestCheckIdDto requestCheckIdDto) {

        ResponseCheckIdDto responseCheckIdDto = new ResponseCheckIdDto();

        if (memberService.checkId(requestCheckIdDto)) {
            responseCheckIdDto.setMessage("중복된 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckIdDto);
        } else {
            responseCheckIdDto.setMessage("OK");
            return ResponseEntity.ok(responseCheckIdDto);
        }
    }

    @PostMapping("/local-login")
    public ResponseEntity<?> localLogin(@RequestBody RequestLocalLoginDto loginDto) {
        Map<String, Object> resultMap = memberService.localLogin(loginDto);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

}

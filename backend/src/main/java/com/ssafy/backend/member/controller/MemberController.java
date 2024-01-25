package com.ssafy.backend.member.controller;

import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import com.ssafy.backend.member.dto.response.ResponseCheckIdDto;
import com.ssafy.backend.member.dto.response.ResponseCheckNicknameDto;
import com.ssafy.backend.member.dto.response.ResponseLocalSignupDto;
import com.ssafy.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
            return ResponseEntity.status(HttpStatus.OK).body(responseCheckIdDto);
        }
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<ResponseCheckNicknameDto> CheckNickname(@RequestBody RequestCheckNicknameDto requestCheckNicknameDto) {

        ResponseCheckNicknameDto responseCheckNicknameDto = new ResponseCheckNicknameDto();

        if (memberService.checkNickname(requestCheckNicknameDto)) {
            responseCheckNicknameDto.setMessage("중복된 닉네임입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckNicknameDto);
        } else {
            responseCheckNicknameDto.setMessage("OK");
            return ResponseEntity.status(HttpStatus.OK).body(responseCheckNicknameDto);
        }
    }

    @PostMapping("/local-signup")
    public ResponseEntity<ResponseLocalSignupDto> localSignup(@RequestBody RequestLocalSignupDto requestLocalSignupDto) {

        ResponseLocalSignupDto responseLocalSignupDto = new ResponseLocalSignupDto();

        if (memberService.localSignup(requestLocalSignupDto) == null) {
            responseLocalSignupDto.setMessage("회원가입 실패!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        } else {
            responseLocalSignupDto.setMessage("OK");
            return ResponseEntity.status(HttpStatus.OK).body(responseLocalSignupDto);
        }
    }

    @PostMapping("/local-login")
    public ResponseEntity<?> localLogin(@RequestBody RequestLocalLoginDto loginDto) {
        Map<String, Object> resultMap = memberService.localLogin(loginDto);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

}

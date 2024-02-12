package com.ssafy.backend.member.controller;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import com.ssafy.backend.member.dto.mapping.StreakMapping;
import com.ssafy.backend.member.dto.request.*;
import com.ssafy.backend.member.dto.response.*;
import com.ssafy.backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/check-id")
    public ResponseEntity<ResponseCheckIdDto> memberCheckId(@RequestBody RequestCheckIdDto requestCheckIdDto) {

        ResponseCheckIdDto responseCheckIdDto;

        if (requestCheckIdDto.getId() == null) {
            responseCheckIdDto = ResponseCheckIdDto.builder()
                    .message("id is null")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckIdDto);
        }

        if (requestCheckIdDto.getId().isEmpty()) {
            responseCheckIdDto = ResponseCheckIdDto.builder()
                    .message("id is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckIdDto);
        }

        return memberService.memberCheckId(requestCheckIdDto);
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<ResponseCheckNicknameDto> memberCheckNickname(@RequestBody RequestCheckNicknameDto requestCheckNicknameDto) {

        ResponseCheckNicknameDto responseCheckNicknameDto;

        if (requestCheckNicknameDto.getNickname() == null) {
            responseCheckNicknameDto = ResponseCheckNicknameDto.builder()
                    .message("nickname is null")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckNicknameDto);
        }

        if (requestCheckNicknameDto.getNickname().isEmpty()) {
            responseCheckNicknameDto = ResponseCheckNicknameDto.builder()
                    .message("nickname is empty")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCheckNicknameDto);
        }

        return memberService.memberCheckNickname(requestCheckNicknameDto);
    }

    @PostMapping("/local-signup")
    public ResponseEntity<ResponseLocalSignupDto> memberLocalSignup(
            RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException {

        ResponseLocalSignupDto responseLocalSignupDto;

        // null check
        if (requestLocalSignupDto.isAnyFieldNull()) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("null data not allowed")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }
        // empty check
        if (requestLocalSignupDto.isAnyFieldEmpty()) {
            responseLocalSignupDto = ResponseLocalSignupDto.builder()
                    .message("empty data not allowed")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseLocalSignupDto);
        }

        return memberService.memberLocalSignup(requestLocalSignupDto);
    }

    @PostMapping("/local-login")
    public ResponseEntity<?> localLogin(@RequestBody RequestLocalLoginDto loginDto) throws NoSuchAlgorithmException {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> data = memberService.localLogin(loginDto);

            HttpHeaders headers = new HttpHeaders();

            headers.add("atk", (String) data.get("atk"));
            data.remove("atk");
            headers.add("rtk", (String) data.get("rtk"));
            data.remove("rtk");

            resultMap.put("data", data);
            resultMap.put("message", "OK");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resultMap);

        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>();

        Long seq = (Long) request.getAttribute("seq");

        memberService.logout(seq);
        resultMap.put("message", "OK");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) { // rtk만 갖고온 사람한테 atk 재발급해줘야됨
        Map<String, String> resultMap = new HashMap<>();

        try {
            Map<String, String> returnMap = memberService.reissue(request);
            HttpHeaders headers = new HttpHeaders();

            headers.add("atk", returnMap.get("atk"));
            returnMap.remove("atk");
            headers.add("rtk", returnMap.get("rtk"));
            returnMap.remove("rtk");

            resultMap.put("message", "OK");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resultMap);
        } catch (Exception e) {
            resultMap.put("message", "세션이 만료되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");
        try {
            ResponseMypageDto responseMypageDto = memberService.mypage(memberSeq);
            resultMap.put("data", responseMypageDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @GetMapping("/mystreak")
    public ResponseEntity<?> myStreak(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");
        try {
            List<StreakMapping> list = memberService.myStreak(memberSeq);
            resultMap.put("data", list);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("modify-info")
    public ResponseEntity<?> modifyInfo(HttpServletRequest request, RequestModifyInfoDto requestModifyInfoDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");
        try {
            memberService.modifyInfo(memberSeq, requestModifyInfoDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/modify-password")
    public ResponseEntity<?> modifyPassword(HttpServletRequest request, @RequestBody RequestModifyPasswordDto requestModifyPasswordDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");
        try {
            memberService.modifyPassword(memberSeq, requestModifyPasswordDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, @RequestBody RequestDeleteDto requestDeleteDto) {
        Map<String, Object> resultMap = new HashMap<>();

        Long memberSeq = (Long) request.getAttribute("seq");
        try {
            memberService.delete(memberSeq, requestDeleteDto);
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("message", "OK");
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

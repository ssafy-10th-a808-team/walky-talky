package com.ssafy.backend.member.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import com.ssafy.backend.member.dto.response.ResponseCheckIdDto;
import com.ssafy.backend.member.dto.response.ResponseCheckNicknameDto;
import com.ssafy.backend.member.dto.response.ResponseLocalSignupDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseMemberDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface MemberService {
    ResponseEntity<ResponseCheckIdDto> memberCheckId(RequestCheckIdDto requestCheckIdDto);

    ResponseEntity<ResponseCheckNicknameDto> memberCheckNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    ResponseEntity<ResponseLocalSignupDto> memberLocalSignup(RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException;

    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) throws WTException;

    public void logout(Long seq);

    public Map<String, String> reissue(Long seq);


    ResponseMemberDto getMemberNicknameUrl(Long memberSeq);


}

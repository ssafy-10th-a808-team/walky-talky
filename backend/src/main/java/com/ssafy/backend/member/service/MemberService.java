package com.ssafy.backend.member.service;

import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface MemberService {

    boolean memberCheckId(RequestCheckIdDto requestCheckIdDto);

    boolean memberCheckNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    boolean localSignup(RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException;

    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) throws NoSuchAlgorithmException;

    public void logout(Long seq);

    public Map<String, String> reissue(Long seq);

}

package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface MemberService {

    boolean checkId(RequestCheckIdDto requestCheckIdDto);

    public Map<String, String> localLogin(RequestLocalLoginDto loginDto);

    public void logout(Long seq);

    boolean checkNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    void localSignup(MultipartFile multipartFile, RequestLocalSignupDto requestLocalSignupDto) throws IOException;

    public Map<String, String> reissue(Long seq);

}

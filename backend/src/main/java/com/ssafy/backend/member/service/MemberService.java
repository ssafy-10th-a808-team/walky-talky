package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;

import java.util.Map;

public interface MemberService {

    boolean checkId(RequestCheckIdDto requestCheckIdDto);

    public Map<String, String> localLogin(RequestLocalLoginDto loginDto);

    public void logout(Long seq);

    boolean checkNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    Member localSignup(RequestLocalSignupDto requestLocalSignupDto);

}

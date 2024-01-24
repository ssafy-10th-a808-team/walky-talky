package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestCheckNicknameDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;
import com.ssafy.backend.member.dto.request.RequestLocalSignupDto;

import java.util.Map;

public interface MemberService {

    boolean checkId(RequestCheckIdDto requestCheckIdDto);

    Map<String, Object> localLogin(RequestLocalLoginDto loginDto);

    boolean checkNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    Member localSignup(RequestLocalSignupDto requestLocalSignupDto);
}

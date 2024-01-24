package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.request.RequestCheckIdDto;
import com.ssafy.backend.member.dto.request.RequestLocalLoginDto;

import java.util.Map;

public interface MemberService {

    public boolean checkId(RequestCheckIdDto requestCheckIdDto);

    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto);

}

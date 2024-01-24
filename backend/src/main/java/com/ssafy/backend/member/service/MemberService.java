package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.MemberDto;

import java.util.Optional;

public interface MemberService {

    public Member findByMemberId(String memberId);

    public boolean localLogin(MemberDto memberDto);

}

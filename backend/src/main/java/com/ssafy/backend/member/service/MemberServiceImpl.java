package com.ssafy.backend.member.service;

import com.ssafy.backend.member.domain.Member;
import com.ssafy.backend.member.dto.MemberDto;
import com.ssafy.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    public boolean localLogin(MemberDto memberDto) {
        Member member = memberRepository.findByMemberId(memberDto.getMemberId());

        if(member == null){ // 아이디에 해당하는 회원 없을때
            return false;
        }else{
            if(member.isDeleted()){ // 탈퇴한 회원일때
                return false;
            }
            if(!memberDto.getPassword().equals(member.getPassword())){ // 비번 틀렸을때
                return false;
            }
        }
        return true;
    }
}

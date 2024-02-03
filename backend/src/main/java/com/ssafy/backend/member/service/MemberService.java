package com.ssafy.backend.member.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.dto.mapping.MemberSeqMapping;
import com.ssafy.backend.member.dto.request.*;
import com.ssafy.backend.member.dto.response.ResponseCheckIdDto;
import com.ssafy.backend.member.dto.response.ResponseCheckNicknameDto;
import com.ssafy.backend.member.dto.response.ResponseLocalSignupDto;
import com.ssafy.backend.member.dto.response.ResponseMypageDto;
import com.ssafy.backend.shareBoard.dto.response.ResponseMemberDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface MemberService {
    ResponseEntity<ResponseCheckIdDto> memberCheckId(RequestCheckIdDto requestCheckIdDto);

    ResponseEntity<ResponseCheckNicknameDto> memberCheckNickname(RequestCheckNicknameDto requestCheckNicknameDto);

    ResponseEntity<ResponseLocalSignupDto> memberLocalSignup(RequestLocalSignupDto requestLocalSignupDto) throws IOException, NoSuchAlgorithmException;

    public Map<String, Object> localLogin(RequestLocalLoginDto loginDto) throws WTException;

    public void logout(Long seq);

    public Map<String, String> reissue(Long seq);


    ResponseMemberDto getMemberNicknameUrl(Long memberSeq);

    String getRegionCd(Long memberSeq) throws WTException;

    List<MemberSeqMapping> getSimilarMemberList(Long memberSeq) throws WTException;

    ResponseMypageDto mypage(Long memberSeq) throws WTException;

    void modifyInfo(Long memberSeq, RequestModifyInfoDto requestModifyInfoDto) throws WTException;

    void modifyPassword(Long memberSeq, RequestModifyPasswordDto requestModifyPasswordDto) throws WTException;
}

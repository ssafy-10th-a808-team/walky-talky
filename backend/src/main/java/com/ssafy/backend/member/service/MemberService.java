package com.ssafy.backend.member.service;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.member.dto.mapping.StreakMapping;
import com.ssafy.backend.member.dto.request.*;
import com.ssafy.backend.member.dto.response.*;
import com.ssafy.backend.shareBoard.dto.response.ResponseMemberDto;
import jakarta.servlet.http.HttpServletRequest;
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

    public Map<String, String> reissue(HttpServletRequest request);

    ResponseMemberDto getMemberNicknameUrl(Long memberSeq);

    String getRegionCd(Long memberSeq) throws WTException;

    List<Long> getSimilarMemberList(Long memberSeq) throws WTException;

    ResponseMypageDto mypage(Long memberSeq) throws WTException;

    List<StreakMapping> myStreak(Long memberSeq) throws WTException;

    void modifyInfo(Long memberSeq, RequestModifyInfoDto requestModifyInfoDto) throws WTException;

    void modifyPassword(Long memberSeq, RequestModifyPasswordDto requestModifyPasswordDto) throws WTException;

    void delete(Long memberSeq, RequestDeleteDto requestDeleteDto) throws WTException;
}

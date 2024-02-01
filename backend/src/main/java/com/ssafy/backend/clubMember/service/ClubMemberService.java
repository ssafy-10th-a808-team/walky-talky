package com.ssafy.backend.clubMember.service;

import com.ssafy.backend.clubMember.dto.request.*;
import com.ssafy.backend.clubMember.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ClubMemberService {
    ResponseClubMemberApplyDto clubMemberApply(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest);

    ResponseClubMemberApplyDto clubMemberApplyCancel(RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest);

    ResponseClubMemberApplyListDto clubMemberApplyList(Long clubSeq, HttpServletRequest httpServletRequest);

    ResponseClubMemberApplyAcceptDto clubMemberApplyAccept(RequestClubMemberApplyAcceptDto requestClubMemberApplyAcceptDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseClubMemberApplyRejectDto> clubMemberApplyReject(RequestClubMemberApplyRejectDto requestClubMemberApplyRejectDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseClubMemberExcludeDto> clubMemberExclude(RequestClubMemberExcludeDto requestClubMemberExcludeDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseClubMemberWithdrawDto> clubMemberWithdraw(RequestClubMemberWithdrawDto requestClubMemberWithdrawDto, HttpServletRequest httpServletRequest);
}

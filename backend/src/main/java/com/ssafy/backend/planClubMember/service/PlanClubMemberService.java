package com.ssafy.backend.planClubMember.service;

import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberCancelDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberCancelDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PlanClubMemberService {
    ResponseEntity<ResponsePlanClubMemberApplyDto> planClubMemberApply(RequestPlanClubMemberApplyDto requestPlanClubMemberApplyDto, HttpServletRequest httpServletRequest);

    ResponseEntity<ResponsePlanClubMemberCancelDto> planClubMemberCancel(RequestPlanClubMemberCancelDto requestPlanClubMemberCancelDto, HttpServletRequest httpServletRequest);
}

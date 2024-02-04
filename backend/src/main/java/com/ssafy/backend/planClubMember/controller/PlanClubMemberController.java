package com.ssafy.backend.planClubMember.controller;

import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.request.RequestPlanClubMemberCancelDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberApplyDto;
import com.ssafy.backend.planClubMember.dto.response.ResponsePlanClubMemberCancelDto;
import com.ssafy.backend.planClubMember.service.PlanClubMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/plan-club-member")
public class PlanClubMemberController {

    private final PlanClubMemberService planClubMemberService;

    @PostMapping("/apply")
    public ResponseEntity<ResponsePlanClubMemberApplyDto> planClubMemberApply(
            @RequestBody RequestPlanClubMemberApplyDto requestPlanClubMemberApplyDto,
            HttpServletRequest httpServletRequest) {
        return planClubMemberService.planClubMemberApply(requestPlanClubMemberApplyDto, httpServletRequest);
    }

    @PostMapping("/cancel")
    public ResponseEntity<ResponsePlanClubMemberCancelDto> planClubMemberCancel(
            @RequestBody RequestPlanClubMemberCancelDto requestPlanClubMemberCancelDto,
            HttpServletRequest httpServletRequest) {
        return planClubMemberService.planClubMemberCancel(requestPlanClubMemberCancelDto, httpServletRequest);
    }

}

package com.ssafy.backend.clubMember.controller;

import com.ssafy.backend.clubMember.dto.request.RequestClubMemberApplyDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyDto;
import com.ssafy.backend.clubMember.service.ClubMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/club-member/")
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    @PostMapping("/apply")
    public ResponseEntity<ResponseClubMemberApplyDto> clubMemberApply(@RequestBody RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {

        ResponseClubMemberApplyDto responseClubMemberApplyDto = clubMemberService.clubMemberApply(requestClubMemberApplyDto, httpServletRequest);

        if (responseClubMemberApplyDto.getMessage().equals("OK")) {
            return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberApplyDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyDto);
        }
    }

    @PostMapping("/apply-cancel")
    public ResponseEntity<ResponseClubMemberApplyDto> clubMemberApplyCancel(@RequestBody RequestClubMemberApplyDto requestClubMemberApplyDto, HttpServletRequest httpServletRequest) {

        ResponseClubMemberApplyDto responseClubMemberApplyDto = clubMemberService.clubMemberApplyCancel(requestClubMemberApplyDto, httpServletRequest);

        if (responseClubMemberApplyDto.getMessage().equals("OK")) {
            return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberApplyDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyDto);
        }
    }
}

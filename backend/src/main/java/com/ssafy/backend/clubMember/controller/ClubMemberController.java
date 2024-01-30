package com.ssafy.backend.clubMember.controller;

import com.ssafy.backend.clubMember.dto.request.RequestClubMemberApplyAcceptDto;
import com.ssafy.backend.clubMember.dto.request.RequestClubMemberApplyDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyAcceptDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyDto;
import com.ssafy.backend.clubMember.dto.response.ResponseClubMemberApplyListDto;
import com.ssafy.backend.clubMember.service.ClubMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/apply/list")
    public ResponseEntity<ResponseClubMemberApplyListDto> clubMemberApplyList(@RequestParam("clubSeq") Long clubSeq, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyListDto responseClubMemberApplyDto = clubMemberService.clubMemberApplyList(clubSeq, httpServletRequest);

        if (responseClubMemberApplyDto.getMessage().equals("OK")) {
            return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberApplyDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyDto);
        }
    }

    @PostMapping("/apply/accept")
    public ResponseEntity<ResponseClubMemberApplyAcceptDto> clubMemberApplyAccept(@RequestBody RequestClubMemberApplyAcceptDto requestClubMemberApplyAcceptDto, HttpServletRequest httpServletRequest) {
        ResponseClubMemberApplyAcceptDto responseClubMemberApplyAcceptDto = clubMemberService.clubMemberApplyAccept(requestClubMemberApplyAcceptDto, httpServletRequest);

        if (responseClubMemberApplyAcceptDto.getMessage().equals("OK")) {
            return ResponseEntity.status(HttpStatus.OK).body(responseClubMemberApplyAcceptDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseClubMemberApplyAcceptDto);
        }
    }

//    @PostMapping("/apply/reject")
}

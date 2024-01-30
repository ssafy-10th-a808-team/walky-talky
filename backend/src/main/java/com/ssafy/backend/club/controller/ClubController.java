package com.ssafy.backend.club.controller;

import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.RequestClubDeleteDto;
import com.ssafy.backend.club.dto.request.RequestClubModifyDto;
import com.ssafy.backend.club.dto.response.*;
import com.ssafy.backend.club.service.ClubService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/club")
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/check-name")
    public ResponseEntity<ResponseClubCheckNameDto> clubCheckName(@RequestBody RequestClubCheckNameDto requestClubCheckNameDto) {
        return clubService.clubCheckName(requestClubCheckNameDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseClubCreateDto> clubCreate(
            @RequestPart("profileImg") MultipartFile multipartFile,
            @RequestPart("json") RequestClubCreateDto requestClubCreateDto,
            HttpServletRequest httpServletRequest) throws IOException {
        return clubService.clubCreate(multipartFile, requestClubCreateDto, httpServletRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest) {
        return clubService.clubList(httpServletRequest);
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseClubDetailDto> clubDetail(@RequestParam("clubSeq") Long clubSeq) {
        return clubService.clubDetail(clubSeq);
    }

    @PostMapping("/modify")
    public ResponseEntity<ResponseClubModifyDto> clubModify(
            @RequestPart("profileImg") MultipartFile multipartFile,
            @RequestPart("json") RequestClubModifyDto requestClubModifyDto,
            HttpServletRequest httpServletRequest) throws IOException {
        return clubService.clubModify(multipartFile, requestClubModifyDto, httpServletRequest);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseClubDeleteDto> clubDelete(
            @RequestBody RequestClubDeleteDto requestClubDeleteDto,
            HttpServletRequest httpServletRequest) {

        return clubService.clubDelete(requestClubDeleteDto, httpServletRequest);

    }

}



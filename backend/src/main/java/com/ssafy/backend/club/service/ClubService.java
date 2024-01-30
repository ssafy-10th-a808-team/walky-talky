package com.ssafy.backend.club.service;

import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.request.RequestClubDeleteDto;
import com.ssafy.backend.club.dto.request.RequestClubModifyDto;
import com.ssafy.backend.club.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClubService {

    ResponseEntity<ResponseClubCreateDto> clubCreate(MultipartFile multipartFile, RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException;

    ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseClubDetailDto> clubDetail(Long clubSeq);

    ResponseEntity<ResponseClubCheckNameDto> clubCheckName(RequestClubCheckNameDto requestClubCheckNameDto);

    ResponseEntity<ResponseClubModifyDto> clubModify(MultipartFile multipartFile,RequestClubModifyDto requestClubModifyDto, HttpServletRequest httpServletRequest) throws IOException;

    ResponseEntity<ResponseClubDeleteDto> clubDelete(RequestClubDeleteDto requestClubDeleteDto, HttpServletRequest httpServletRequest);
}

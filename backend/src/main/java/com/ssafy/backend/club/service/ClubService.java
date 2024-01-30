package com.ssafy.backend.club.service;

import com.ssafy.backend.club.dto.request.RequestClubCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.response.ResponseClubCheckNameDto;
import com.ssafy.backend.club.dto.response.ResponseClubCreateDto;
import com.ssafy.backend.club.dto.response.ResponseClubDetailDto;
import com.ssafy.backend.club.dto.response.ResponseClubListDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClubService {

    ResponseEntity<ResponseClubCreateDto> clubCreate(MultipartFile multipartFile, RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException;

    ResponseEntity<ResponseClubListDto> clubList(HttpServletRequest httpServletRequest);

    ResponseEntity<ResponseClubDetailDto> clubDetail(Long clubSeq);

    ResponseEntity<ResponseClubCheckNameDto> clubCheckName(RequestClubCheckNameDto requestClubCheckNameDto);
}

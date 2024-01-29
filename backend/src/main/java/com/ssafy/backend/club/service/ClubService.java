package com.ssafy.backend.club.service;

import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.dto.response.ResponseClubDetailDto;
import com.ssafy.backend.club.dto.response.ResponseClubListDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClubService {
    boolean checkName(RequestCheckNameDto requestCheckNameDto);

    void clubCreate(MultipartFile multipartFile, RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) throws IOException;

    ResponseClubListDto clubList(HttpServletRequest httpServletRequest);

    ResponseClubDetailDto clubDetail(Long clubSeq);
}

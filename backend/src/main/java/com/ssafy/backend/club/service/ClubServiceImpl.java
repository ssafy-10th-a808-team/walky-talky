package com.ssafy.backend.club.service;

import com.ssafy.backend.club.domain.Club;
import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.dto.request.RequestClubCreateDto;
import com.ssafy.backend.club.repository.ClubRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Override
    public boolean checkName(RequestCheckNameDto requestCheckNameDto) {
        return clubRepository.existsByName(requestCheckNameDto.getName());
    }

    @Override
    public Club clubCreate(RequestClubCreateDto requestClubCreateDto, HttpServletRequest httpServletRequest) {
        Club club = requestClubCreateDto.toEntity();
        Club savedClub = clubRepository.save(club);

        return savedClub;
    }
}

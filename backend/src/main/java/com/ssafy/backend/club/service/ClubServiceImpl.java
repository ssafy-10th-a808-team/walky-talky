package com.ssafy.backend.club.service;

import com.ssafy.backend.club.dto.request.RequestCheckNameDto;
import com.ssafy.backend.club.repository.ClubRepository;
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
}

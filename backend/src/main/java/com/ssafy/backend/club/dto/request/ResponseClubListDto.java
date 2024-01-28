package com.ssafy.backend.club.dto.request;

import com.ssafy.backend.club.domain.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClubListDto {
    private String message;
    private List<Club> myClubs;
    private List<Club> recommendClubs;
    private List<Club> allClubs;
}

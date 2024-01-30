package com.ssafy.backend.club.dto.response;

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
    private List<ResponseClubListDtoClub> myClubs;
    private List<ResponseClubListDtoClub> recommendClubs;
    private List<ResponseClubListDtoClub> otherClubs;
}

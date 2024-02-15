package com.ssafy.backend.club.dto.response;

import com.ssafy.backend.club.domain.Club;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseClubListDto {
    private String message;
    private List<ResponseClubListDtoClub> myClubs;
    private List<ResponseClubListDtoClub> recommendClubs;
    private List<ResponseClubListDtoClub> otherClubs;
}

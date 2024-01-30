package com.ssafy.backend.club.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseClubDetailDtoMember {
    private String nickname;
    private String url;
    private String address;
    private String role;
}
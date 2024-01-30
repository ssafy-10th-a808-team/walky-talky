package com.ssafy.backend.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestClubModifyDto {
    private Long clubSeq;
    private String name;
    private String introduce;
    private String regionCd;
    private String young_birth;
    private String old_birth;
    private String gender_type;
    private Integer max_capacity;
    private Boolean is_auto_recruit;
    private Boolean is_open_recruit;
}

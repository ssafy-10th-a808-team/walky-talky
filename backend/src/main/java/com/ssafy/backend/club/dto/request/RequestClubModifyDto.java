package com.ssafy.backend.club.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class RequestClubModifyDto {

    private MultipartFile multipartFile;
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

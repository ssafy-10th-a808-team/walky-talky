package com.ssafy.backend.club.dto.request;

import com.ssafy.backend.club.domain.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestClubCreateDto {

    private String name;
    private String introduce;
    private String address;
    private String young_birth;
    private String old_birth;
    private String gender_type;
    private int max_capacity;
    private boolean is_auto_recruite;

    public Club toEntity() {
        return Club.builder()
                .seq(null)
                .name(name)
                .url("바꿔야 함")
                .introduce(introduce)
                .regionCd("바꿔야 함")
                .youngBirth(young_birth)
                .oldBirth(old_birth)
                .genderType(gender_type)
                .nowCapacity(1)
                .maxCapacity(max_capacity)
                .isAutoRecruite(is_auto_recruite)
                .isOpenRecruite(true)
                .isDeleted(false)
                .build();
    }
}

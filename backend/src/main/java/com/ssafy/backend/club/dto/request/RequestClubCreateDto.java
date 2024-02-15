package com.ssafy.backend.club.dto.request;

import com.ssafy.backend.club.domain.Club;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestClubCreateDto {

    private MultipartFile multipartFile;
    private String name;
    private String introduce;
    private String regionCd;
    private String young_birth;
    private String old_birth;
    private String gender_type;
    private Integer max_capacity;
    private Boolean is_auto_recruit;

    public Club toEntity() {
        return Club.builder()
                .seq(null)
                .name(name)
                .url(null)
                .introduce(introduce)
                .regionCd(regionCd)
                .youngBirth(young_birth)
                .oldBirth(old_birth)
                .genderType(gender_type)
                .nowCapacity(1)
                .maxCapacity(max_capacity)
                .isAutoRecruit(is_auto_recruit)
                .isOpenRecruit(true)
                .isDeleted(false)
                .build();
    }

    // Null 체크 메서드
    public boolean isNull(Object value) {
        return value == null;
    }

    // Empty 체크 메서드
    public boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }

        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }

        if (value instanceof Map) {
            return ((Map<?, ?>) value).isEmpty();
        }

        if (value.getClass().isArray()) {
            return ((Object[]) value).length == 0;
        }

        return false;
    }

    // DTO 전체의 Null 체크
    public boolean isAnyFieldNull() {
        return isNull(name)
                || isNull(regionCd)
                || isNull(young_birth)
                || isNull(old_birth)
                || isNull(gender_type)
                || isNull(max_capacity)
                || isNull(is_auto_recruit);
    }

    // DTO 전체의 Empty 체크
    public boolean isAnyFieldEmpty() {
        return isEmpty(name)
                || isEmpty(regionCd)
                || isEmpty(young_birth)
                || isEmpty(old_birth)
                || isEmpty(gender_type)
                || isEmpty(max_capacity)
                || isEmpty(is_auto_recruit);
    }
}

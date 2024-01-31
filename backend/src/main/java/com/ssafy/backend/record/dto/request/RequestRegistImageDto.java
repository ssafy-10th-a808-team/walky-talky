package com.ssafy.backend.record.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegistImageDto {
    private Long seq;
    private MultipartFile multipartFile;
    private String latitude;
    private String longitude;
}
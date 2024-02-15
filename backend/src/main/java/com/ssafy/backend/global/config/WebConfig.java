package com.ssafy.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")
//                .allowedOrigins(
//                        "https://i10a808.p.ssafy.io",
//                        "http://localhost:5173"
//                )
//                .allowedMethods("GET", "POST", "OPTIONS")
//                .exposedHeaders("atk", "rtk");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://i10a808.p.ssafy.io", "http://localhost:5173")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowCredentials(true) // 이 줄을 추가
                .exposedHeaders("atk", "rtk");
    }
}


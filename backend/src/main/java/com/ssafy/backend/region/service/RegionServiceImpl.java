package com.ssafy.backend.region.service;

import com.ssafy.backend.region.domain.Region;
import com.ssafy.backend.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public String findRegionCode(String address) {
        // 주소로 지역코드 찾기
        // 예제 : findRegionCode("서울특별시 강남구 역삼동") > 11680101
        Region region = regionRepository.findRegionCdByLocataddNm(address);
        return region.getRegionCd();
    }

    public String findAddress(String regionCode) {
        // 지역코드로 주소 찾기
        // 예제 : findAddress(11680101) > 서울특별시 강남구 역삼동
        Region region = regionRepository.findLocataddNmByRegionCd(regionCode);
        return region.getLocataddNm();
    }

}

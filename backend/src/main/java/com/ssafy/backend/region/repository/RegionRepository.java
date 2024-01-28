package com.ssafy.backend.region.repository;

import com.ssafy.backend.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    int findRegionCdByLocataddNm(String locataddNm);

    String findLocataddNmByRegionCd(int regionCd);

}

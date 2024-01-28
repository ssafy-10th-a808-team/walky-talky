package com.ssafy.backend.region.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@Getter
@NoArgsConstructor
@Builder
public class Region {

    @Id
    private Integer regionCd;
    private String locataddNm;
    private String sidoo;
    private String sgg;
    private String locallowNm;

    public Region(Integer regionCd, String locataddNm, String sidoo, String sgg, String locallowNm) {
        this.regionCd = regionCd;
        this.locataddNm = locataddNm;
        this.sidoo = sidoo;
        this.sgg = sgg;
        this.locallowNm = locallowNm;
    }
}

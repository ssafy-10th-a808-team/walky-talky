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
    private String regionCd;
    private String locationAddr;

    public Region(String regionCd, String locationAddr) {
        this.regionCd = regionCd;
        this.locationAddr = locationAddr;
    }
}

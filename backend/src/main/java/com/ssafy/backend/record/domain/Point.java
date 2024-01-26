package com.ssafy.backend.record.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class Point {
    private Double lat;
    private Double lng;

    @Override
    public String toString() {
        return "lat : " + lat + "lng : " + lng;
    }
}

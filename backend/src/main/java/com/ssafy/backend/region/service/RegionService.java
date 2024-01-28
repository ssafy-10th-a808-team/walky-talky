package com.ssafy.backend.region.service;

public interface RegionService {

    int findRegionCode(String address);

    String findAddress(int regionCode);
}

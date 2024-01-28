package com.ssafy.backend.region.service;

public interface RegionService {

    String findRegionCode(String address);

    String findAddress(String regionCode);
}

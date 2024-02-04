package com.ssafy.backend.oauth.service;

import java.util.Map;

public interface OauthService {

    Map<String, Object> login(String code);
}

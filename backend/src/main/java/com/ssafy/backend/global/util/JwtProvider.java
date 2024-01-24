package com.ssafy.backend.global.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import java.util.Base64;

@Component
public class JwtProvider {

    @Value("${security.salt}")
    private String salt;


    public String createAccessToken(String memberId, Long tokenLive) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, salt)
                .compact();

        return token;
    }

    public String createRefreshToken(String memberId, Long tokenLive) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, salt)
                .compact();

        return token;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(salt).parseClaimsJws(jwtToken);
            return !(claims.getBody().getExpiration().before(new Date()));
        } catch (Exception e) {
            return false;
        }
    }


}

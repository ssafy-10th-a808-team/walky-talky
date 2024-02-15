package com.ssafy.backend.global.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${security.salt}")
    private String salt;

    private final String issuer = "walkytalky";

    public String createAccessToken(Long seq, Long tokenLive) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("seq", seq);

        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, salt)
                .compact();

        return token;
    }

    public String createRefreshToken(Long seq, Long tokenLive) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("seq", seq);

        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, salt)
                .compact();

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(salt)
                    .parseClaimsJws(token);
            return !(claims.getBody().getExpiration().before(new Date()) && issuer.equals(claims.getBody().getIssuer()));
        } catch (Exception e) {
            return false;
        }
    }

    public Long getSeq(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("seq", Long.class);
    }

}

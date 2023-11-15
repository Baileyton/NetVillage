package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    public String secretKey;


    @Value("${jwt.validitySeconds}")
    public long validitySeconds; // 1시간

    public String createToken(String nick){
        Claims claims = Jwts.claims().setSubject(nick);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validitySeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

package com.pavan.jwtDemo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret ="PriaccInnovationsoiafhoviasdnlkjasdoufnsaldihflskdhcaushedflkanwielugf";

    private Key getSignature(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateJwtToke(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*6060))
                .signWith(getSignature(), SignatureAlgorithm.HS256)
                .compact();
    }

}
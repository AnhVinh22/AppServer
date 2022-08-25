package com.example.demo.service;

import com.example.demo.constant.ERole;
import com.example.demo.util.JwtData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String KEY = "a-very-secure-server-key-of-phoenix";

    public String generateToken(String userId, String email, ERole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
        String token = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, KEY.getBytes())
                .compact();
        return token;
    }

    public String parseTokenToRole(String token) {
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ","");
        Claims claims = Jwts.parserBuilder().setSigningKey(KEY.getBytes()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    public String parseTokenToUserId(String token) {
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ","");
        Claims claims = Jwts.parserBuilder().setSigningKey(KEY.getBytes()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("userId").toString();
    }


    public Map<String, Object> parseTokenToClaims(String token) {
        token = token.replace("Bearer ","");
        Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(KEY.getBytes()).build().
                parseClaimsJws(token).getBody();
        return claims;
    }


    public JwtData parseToken(String token) {
        token = token.replace("Bearer ","");
        Claims claims = Jwts.parserBuilder().setSigningKey(KEY.getBytes()).build().
                parseClaimsJws(token).getBody();
        JwtData result = JwtData.builder()
                .userId(claims.get("userId").toString())
                .email(claims.get("email").toString())
                .role(ERole.valueOf(claims.get("role").toString()))
                .build();
        return result;
    }

}

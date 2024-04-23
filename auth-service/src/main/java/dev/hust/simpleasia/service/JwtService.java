package dev.hust.simpleasia.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final String secretKey = "mysupersuperlonglongsecurekey";

    private final SecretKey signingKey = new SecretKeySpec(
            DatatypeConverter.parseBase64Binary(secretKey),
            SignatureAlgorithm.HS512.getJcaName()
    );

    @Bean
    public SecretKey signingKey() {
        return new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey),
                SignatureAlgorithm.HS512.getJcaName());
    }

    public void verifyToken(String token) {
        Jwts.parser().setSigningKey(signingKey)
                .parseClaimsJws(token);
    }

    public String createToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        Date now = new Date(System.currentTimeMillis());
        long expireTime = 24 * 60 * 60 * 1000;
        Date expiredDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}

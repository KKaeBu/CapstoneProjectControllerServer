package com.server.controlserver.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
  @Value("${jwt.password}") /* SECRET_KEY */
  private String secretKey;

  /*토큰 생성 메소드*/
  public String createToken(String subject) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + Duration.ofMinutes(30).toMillis()); // 만료기간 30분
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1) 토큰헤더에 JWT 속성임을 기재함
        .setIssuer("test") // 토큰발급자(iss)
        .setIssuedAt(now) // 발급시간(iat)
        .setExpiration(expiration) // 만료시간(exp)
        .setSubject(subject) //  토큰 제목(subject)
        .signWith(
            SignatureAlgorithm.HS256,
            Base64.getEncoder().encodeToString(secretKey.getBytes())) // 알고리즘, 시크릿 키
        .compact();
  }

  /*Jwt 토큰의 유효성 체크 메소드*/
  public String parseJwtToken(String token) {
    token = BearerRemove(token); // Bearer 제거
    try {
      return Jwts.parser()
          .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (ExpiredJwtException e) {
      System.out.println("토큰 만료");
      return null;
    } catch (JwtException e) {
      System.out.println("토큰 에러");
      return null;
    }
  }

  /*토큰 앞 부분('Bearer') 제거 메소드*/
  private String BearerRemove(String token) {
    return token.substring("Bearer ".length());
  }
}

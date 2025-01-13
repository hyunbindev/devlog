package com.devlog.devlog.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.devlog.devlog.exception.JwtCustomException;

import java.util.Date;
@Slf4j
@Component
public class JwtTokenProvider {
    //테스트를 위해 30초
    private final long  tokenValidTime = 30 * 60 * 1000L;
    //private final long  tokenValidTime =  1000L;
    private final long refreshTokenValidTime = 30 * 60* 1000L;
    private final String ACCESSKEY ="YourLongSecretKeyThatIsAt56165186541689418498513254987561325574862184328712184jkhLeast256BitsLong";
    private final String REFRESHKEY ="YourLongSecretKeyThatIsAt56165186541689418498513254987561325574862184328712184jkhLeast256BitsLong";
    public String createAccessToken(String memberId){
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(memberId);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,ACCESSKEY)
                .compact();
        return token;
    }
    public String createRefreshToken(String memberId) {
    	Date now = new Date();
    	Claims claims = Jwts.claims().setSubject(memberId);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256 , REFRESHKEY)
                .compact();
        return refreshToken;
    }
    
    //엑세스 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
    	Claims claims = parseToken(refreshToken, REFRESHKEY);
        return createAccessToken(claims.getSubject());
    }
    //토큰 유효성 검증
    public String getUser(String accessToken) {
        Claims claims = parseToken(accessToken, ACCESSKEY);
        return claims.getSubject();
    }
    
    public String validationRefreshToken(String accessToken) {
        try {
        	return parseToken(accessToken, REFRESHKEY).getSubject();
        }catch(Exception e) {
        	return null;
        }
    }
    
    private Claims parseToken(String token, String key) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtCustomException.TokenExpiredException(e);
        } catch (JwtException e) {
            throw new JwtCustomException.TokenInvalidException(e);
        } catch (Exception e) {
            throw new JwtCustomException.TokenUnexpectedException(e);
        }
    }
}

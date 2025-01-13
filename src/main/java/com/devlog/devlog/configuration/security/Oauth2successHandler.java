package com.devlog.devlog.configuration.security;

import com.devlog.devlog.utils.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class Oauth2successHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public Oauth2successHandler(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = jwtTokenProvider.createAccessToken(authentication.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication.getName());
        Cookie accessTokenCookie = new Cookie("Authorization", accessToken);
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        accessTokenCookie = preprocessCookie(accessTokenCookie);
        refreshTokenCookie = preprocessCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

    }
    private Cookie preprocessCookie(Cookie cookie) {
    	cookie.setPath("/");
    	cookie.setHttpOnly(true);
    	return cookie;
    }
}
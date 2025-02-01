package com.devlog.devlog.configuration.security;

import com.devlog.devlog.controller.JournalController;
import com.devlog.devlog.data.dto.CustomOAuth2User;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.utils.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2successHandler implements AuthenticationSuccessHandler {
    //private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*
         * 세션으로 전환
    	String accessToken = jwtTokenProvider.createAccessToken(authentication.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication.getName());
        Cookie accessTokenCookie = new Cookie("Authorization", accessToken);
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        accessTokenCookie = preprocessCookie(accessTokenCookie);
        refreshTokenCookie = preprocessCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        */
    	CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    	MemberDTO memberDTO = new MemberDTO(oAuth2User.getAttributes());
    	memberDTO.setId(authentication.getName());
    	MemberDTO validMemberDTO = memberService.getMember(memberDTO);
    	if(!validMemberDTO.isMember()) {
    		response.sendRedirect("/signup");
    		return;
    	}
    	response.sendRedirect("/journal/"+memberDTO.getId());
    }
    
    @Deprecated
    private Cookie preprocessCookie(Cookie cookie) {
    	cookie.setPath("/");
    	cookie.setHttpOnly(true);
    	return cookie;
    }
}
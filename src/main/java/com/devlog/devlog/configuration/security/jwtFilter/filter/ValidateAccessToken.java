package com.devlog.devlog.configuration.security.jwtFilter.filter;

import com.devlog.devlog.data.dto.CustomOAuth2User;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.exception.JwtCustomException;
import com.devlog.devlog.utils.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
public class ValidateAccessToken extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final String[] publicURLs;
    public ValidateAccessToken(JwtTokenProvider jwtTokenProvider , String[] publicURLs){
        this.jwtTokenProvider = jwtTokenProvider;
        this.publicURLs = publicURLs;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	String requestURI = request.getRequestURI();
    	//public url 혹은 dev url이 들어올시 필터 건너뜀

    	for(String url :publicURLs) {
    		//publicURLs
    		if(url.equals(requestURI)) {
    			log.info("match!!");
    			filterChain.doFilter(request, response);
    			return;
    		}
    	}
		
    	Cookie[] cookies = request.getCookies();
        String token = null;
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie :cookies){
                if(cookie.getName().equals("Authorization")){
                    token = cookie.getValue();
                }
                if(cookie.getName().equals("RefreshToken")) {
                	refreshToken = cookie.getValue();
                }
            }
        }
        
        if(token == null){
        	filterChain.doFilter(request, response);
        	return;
        }
        
        try{
            String memberId = jwtTokenProvider.getUser(token);
            MemberDTO dto = new MemberDTO();
            dto.setId(memberId);
            CustomOAuth2User user = new CustomOAuth2User(dto);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JwtCustomException.TokenExpiredException e){
            //토큰 만료
        	try {
        		String newAccessToken = refreshAccessToken(response , refreshToken);
        		MemberDTO dto = new MemberDTO();
        		CustomOAuth2User user = new CustomOAuth2User(dto);
        		Authentication authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        		SecurityContextHolder.getContext().setAuthentication(authentication);
        		Cookie cookie = new Cookie("Authorization",newAccessToken);
            	cookie.setPath("/");
            	cookie.setHttpOnly(true);
        		response.addCookie(cookie);
        	}catch(Exception refreshError){
        		throw e;
        	}
        }catch (JwtCustomException.TokenInvalidException e) {
        	//토큰 위조
        	throw e;
        }catch (JwtCustomException.TokenUnexpectedException e) {
        	throw e;
        }finally {
        	filterChain.doFilter(request,response);
        }
    }
    //엑세스토큰 재발급
    //결국 재발급이 불가능하면 다시로그인 해야함
    //불가능할때 null을 리턴하면
    private String refreshAccessToken(HttpServletResponse response ,String refreshToken) {
    	try {
    		return jwtTokenProvider.refreshAccessToken(refreshToken);
    	}catch (JwtCustomException.TokenExpiredException e){
            //토큰 만료
        	throw e;
        }catch (JwtCustomException.TokenInvalidException e) {
        	//토큰 위조
        	throw e;
        }catch (JwtCustomException.TokenUnexpectedException e) {
        	//
        	throw e;
        }
    }
}

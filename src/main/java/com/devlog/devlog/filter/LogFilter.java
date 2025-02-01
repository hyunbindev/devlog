package com.devlog.devlog.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;
import com.devlog.devlog.service.GlobalLogService.GlobalLog;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class LogFilter extends OncePerRequestFilter{
	private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
	private final GlobalLog globalLog;
	@Override
    public void destroy() {
        logger.info("전역 로그 필터 종료");
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		filterChain.doFilter(request, response);

		globalLog.writeLog(request,response);
	}
}

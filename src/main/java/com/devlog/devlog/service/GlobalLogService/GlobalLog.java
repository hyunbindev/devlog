package com.devlog.devlog.service.GlobalLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GlobalLog {
	private static final Logger logger = LoggerFactory.getLogger(GlobalLog.class);
	
	@Async
	public void writeLog(HttpServletRequest request, HttpServletResponse response) {
		try {
		}catch(Exception e) {
			logger.error("로그 기록 중 오류 발생", e);
		}
	}
}

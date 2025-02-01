package com.devlog.devlog.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.devlog.devlog.interceptor.JournalInterceptor;
import com.devlog.devlog.service.UserTrackingService.UserTrackingService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	private final JournalInterceptor journalInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		 registry.addInterceptor(journalInterceptor).addPathPatterns("/journal/**");
	}
}

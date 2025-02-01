package com.devlog.devlog.service.UserTrackingService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTrackingScheduler {
	private final UserTrackingService userTrackingService;
	
	//한국 서울 기준 0시에 사용자 기록 초기화
	@Scheduled(cron = "0 0 0 * * *" , zone = "Asia/Seoul")
	//@Scheduled(fixedRate = 10000)
	public void clearUserVisit() {
		userTrackingService.clearVisitor();
		log.info("clearVisitor");
	}
}

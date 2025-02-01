package com.devlog.devlog.controller.restapi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.data.dto.VisitorLogHistoryDTO;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.UserTrackingService.visitorlog.LogHistoryService;
import com.devlog.devlog.service.UserTrackingService.visitorlog.VisitorLogSevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {
	private final VisitorLogSevice visitorLogService;
	private final LogHistoryService logHistoryService;
	@GetMapping("/visitor/total")
	public ResponseEntity<Integer> getTotalVisitor(Authentication authentication) {
		Integer totalVisitorCount =  visitorLogService.getTotalVisitorCountView(authentication.getName());
		return new ResponseEntity<Integer>(totalVisitorCount , HttpStatus.OK);
	}
	@GetMapping("/visitor/month")
	public ResponseEntity<List<VisitorLogHistoryDTO>> getVisitorMont(Authentication authentication) {
		List<VisitorLogHistoryDTO> historyDTOs = logHistoryService.getVisitorDayHistory(authentication.getName() , 30);
		return new ResponseEntity<List<VisitorLogHistoryDTO>>(historyDTOs , HttpStatus.OK);
	}
}
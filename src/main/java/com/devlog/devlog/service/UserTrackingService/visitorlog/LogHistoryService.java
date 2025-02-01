package com.devlog.devlog.service.UserTrackingService.visitorlog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
import com.devlog.devlog.data.dto.VisitorLogHistoryDTO;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.VisitorLogEntity;
import com.devlog.devlog.exception.JournalException;
import com.devlog.devlog.repository.MemberRepository;
import com.devlog.devlog.repository.VisitorLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogHistoryService {
	private final MemberRepository memberRepository;
	private final VisitorLogRepository visitorLogRepository;
	
	public List<VisitorLogHistoryDTO> getVisitorDayHistory(String memberId,int term) {
		MemberEntity memberEntity = memberRepository.findById(memberId)
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		LocalDate today = LocalDate.now();
		LocalDate startDay = today.minusDays(term);
		
		List<VisitorLogEntity> logEntitys = visitorLogRepository.findByvisitDateBetweenAndMember(startDay, today, memberEntity);
		
		List<VisitorLogHistoryDTO> logDTOs = new ArrayList<>();
		
		for(VisitorLogEntity logEntity : logEntitys) {
			if(logDTOs.isEmpty()) {
				logDTOs.add(logEntityMapping(logEntity));
				continue;
			}
			
			//리스트 마지막원소
			VisitorLogHistoryDTO lastLogOfArray = logDTOs.get(logDTOs.size()-1);
			//DTO리스트의 마지막 원소 날짜가 같다면
			if(lastLogOfArray.getDate().equals(logEntity.getVisitDate())) {
				lastLogOfArray.incrementCount();
			}else {
				logDTOs.add(logEntityMapping(logEntity));
			}
		}
		return logDTOs;
	}
	private VisitorLogHistoryDTO logEntityMapping(VisitorLogEntity entity) {
		VisitorLogHistoryDTO dto = new VisitorLogHistoryDTO(entity.getVisitDate());
		return dto;
	}
}

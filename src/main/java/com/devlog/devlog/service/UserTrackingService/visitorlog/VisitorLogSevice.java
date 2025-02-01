package com.devlog.devlog.service.UserTrackingService.visitorlog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
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
public class VisitorLogSevice {
	private final VisitorLogRepository visitorLogRepository;
	private final MemberRepository memberRepository;
	
	public void createLog(MemberEntity memberEntity , String referrer) {
		VisitorLogEntity visitorLogEntity = new VisitorLogEntity(memberEntity);
		visitorLogEntity.setReferrer(referrer);
		visitorLogRepository.save(visitorLogEntity);
	}
	
	public int getTotalVisitorCountView(String memberId) {
		MemberEntity memberEntity = memberRepository.findById(memberId)
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		
		return getTotalVisitorCount(memberEntity);
	}
	
	private int getTotalVisitorCount(MemberEntity memberEntity) {
		return visitorLogRepository.countBymember(memberEntity);
	}
	
}

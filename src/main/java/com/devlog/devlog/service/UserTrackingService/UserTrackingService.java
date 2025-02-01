package com.devlog.devlog.service.UserTrackingService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.repository.MemberRepository;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;
import com.devlog.devlog.service.UserTrackingService.visitorlog.VisitorLogSevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTrackingService{
	private final VisitorLogSevice visitorLogService;
	private final MemberRepository memberRepository;
	
	private Map<String ,Set<String>> anonymousVisit = new HashMap<>();
	private Map<String, Set<String>> memberVisit = new HashMap<>();
	
	
	public void trackingAnonymousVisit(String targetJournal , String sessionId , String referer) {
		anonymousVisit.computeIfAbsent(targetJournal, k -> new HashSet<>());
		//접속기록이 존재 하지 않다면 로그와 함께 기록
		Set<String> visitSet = anonymousVisit.get(targetJournal);
		if(!visitSet.contains(sessionId)) {
			visitSet.add(sessionId);
			createLog(targetJournal,referer);
		}
		
	}
	
	public void trakingMemberVisit(String targetJournal , String visitorId , String referer) {
		memberVisit.computeIfAbsent(targetJournal, k -> new HashSet<>());
		//접속기록이 존재 하지 않다면 로그와 함께 기록
		Set<String> visitSet = memberVisit.get(targetJournal);
		if(!visitSet.contains(visitorId)) {
			visitSet.add(visitorId);
			createLog(targetJournal,referer);
		}
	}
	 
	public int getJournalVisitorCount(String targetJouarnal) {
		Set<String> anonymousVisitsSet = anonymousVisit.get(targetJouarnal);
		Set<String> memberVisitSet = memberVisit.get(targetJouarnal);
		
		int anonymousVisitCount;
		int memberVisitCount;
		
		 //익명사용자의 방문횟수
		 if(anonymousVisitsSet == null) {
			 anonymousVisitCount=0;
		 }else {
			 anonymousVisitCount = anonymousVisitsSet.size();
		 }
		 //기존사용자의 방문 횟수
		 if(memberVisitSet ==null) {
			 memberVisitCount=0;
		 }else{
			 memberVisitCount = memberVisitSet.size();
		 }

		 return anonymousVisitCount + memberVisitCount;
	}
	//유저사용 기록 초기화
	public void clearVisitor() {
		anonymousVisit.clear();
		memberVisit.clear();
	}
	
	private void createLog(String targetJournal , String referer) {
		Optional<MemberEntity> optionalTargetMember = memberRepository.findById(targetJournal);
		//타겟 사용자가 존재하지 않으면 종료
		if(optionalTargetMember.isEmpty())return;
		
		
		MemberEntity targetMember = optionalTargetMember.get();
		visitorLogService.createLog(targetMember, referer);
	}
}

package com.devlog.devlog.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
import com.devlog.devlog.controller.BoardController;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.exception.JournalException;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;
import com.devlog.devlog.service.UserTrackingService.UserTrackingService;
import com.devlog.devlog.service.UserTrackingService.visitorlog.VisitorLogSevice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JournalInterceptor implements HandlerInterceptor{
	private final UserTrackingService userTrakingService;
	private final VisitorLogSevice visitorLogService;
	private final MemberService memberService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		String targetJournal = request.getRequestURI().split("/")[2];
		MemberDTO memberDTO = new MemberDTO(targetJournal);
		memberDTO = memberService.getMember(memberDTO);
		if(memberDTO.isMember()) {
			return true;
		}
		throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler ,ModelAndView model) throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String targetJournal = request.getRequestURI().split("/")[2];
		String referer = request.getHeader("referer");
		//비로그인 사용자의 추적 및 사용자 소유 페이지 뷰 로직
		if("anonymousUser".equals(authentication.getName())) {
			HttpSession session = request.getSession(true);
			String sessionId = session.getId();
			userTrakingService.trackingAnonymousVisit(targetJournal, sessionId , referer);
			//익명 사용자
			model.addObject("isOwner", false);
		}else {
			//로그인 사용자의 추적 로직
			userTrakingService.trakingMemberVisit(targetJournal , authentication.getName() , referer);
			//사용자 인증 아이디와 타겟 저널이 같으면 소유
			if(authentication.getName().equals(targetJournal)) {
				model.addObject("isOwner",true);
			}else {
				model.addObject("isOwner",false);
			}
		}
		//방문자수
		int todayVisitCount = userTrakingService.getJournalVisitorCount(targetJournal);
		int totalVisitCount = visitorLogService.getTotalVisitorCountView(targetJournal);
		model.addObject("todayVisitCount", todayVisitCount);
		model.addObject("totalVisitCount", totalVisitCount);
	}
}

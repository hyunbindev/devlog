package com.devlog.devlog.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ManageAspect {
	private final MemberService memberService;
	//manage controller 호출전 Model에 사용자 정보 기반memberDTO 추가
	@Pointcut("execution(* com.devlog.devlog.controller.ManageController.*(org.springframework.security.core.Authentication, org.springframework.ui.Model, ..))")
	private void manageAspectPointCut() {}
	
	@Before("manageAspectPointCut()")
    public void setAuthModel(JoinPoint joinPoint) {
		 Object[] arguments = joinPoint.getArgs();
		 Authentication auth = (Authentication) arguments[0]; 
		 auth.getPrincipal();
		 Model model = (Model) arguments[1];
		 MemberDTO memberDTO = new MemberDTO();
		 memberDTO.setId(auth.getName());
		 memberDTO = memberService.getMember(memberDTO);
		 model.addAttribute("memberDTO", memberDTO);
    }
}
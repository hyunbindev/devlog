package com.devlog.devlog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.devlog.devlog.controller.MangeController;
import com.devlog.devlog.data.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Aspect
@Component
public class MemberAspect {
	
	@Around("@annotation(com.devlog.devlog.aspect.annotation.CheckOwner)")
	public Object checkOwner(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] arguments = joinPoint.getArgs();
		Authentication authentication = (Authentication)arguments[0];
		String memberId = (String)arguments[1];
		Model model = (Model)arguments[2];;
		
		if(authentication != null && authentication.getName().equals(memberId)) {
			model.addAttribute("isOwner", true);
		}else {
			model.addAttribute("isOwner",false);
		}
		arguments[2] = model;
		Object returnedByMethod = joinPoint.proceed(arguments);
		
		return returnedByMethod;
	}
	@Before("@annotation(com.devlog.devlog.aspect.annotation.TestAspect)")
	public void test() {
		log.info("Test annotation is invoked");
	}
}

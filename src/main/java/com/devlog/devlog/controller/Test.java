package com.devlog.devlog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.service.MemberService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
@Controller
@Slf4j
@RequestMapping
public class Test {
	private final MemberService memberService;
	
	public Test(MemberService memberService) {
		this.memberService = memberService;
	}
	@GetMapping
	@ResponseBody
	public String testindex() {
		return "main";
	}
	
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
    @GetMapping("/test2")
    @ResponseBody
    public String test2(Authentication authentication, Model model) {
        // 인증되지 않은 경우 처리
        return authentication.getName();
    }

    //테스트
    @GetMapping("/test3")
    public String test3(Authentication authentication, Model model){
    	MemberDTO memberDTO = new MemberDTO();
    	memberDTO.setId( authentication.getName());
    	memberDTO = memberService.getMember(memberDTO);
    	model.addAttribute("memberDTO", memberDTO);
        return "/thymeleaf/sample";
    }
}

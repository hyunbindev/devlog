package com.devlog.devlog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.devlog.devlog.data.dto.CustomOAuth2User;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
	private final MemberService memberService;
	
	@GetMapping
	public String getSignupView(Authentication authentication , Model model) {
    	CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    	MemberDTO signupMemberDTO = new MemberDTO(oAuth2User.getAttributes());
		model.addAttribute("name",signupMemberDTO.getName());
		model.addAttribute("bio", signupMemberDTO.getBio());
		model.addAttribute("email", signupMemberDTO.getEmail());
		return "thymeleaf/journal/signup";
	}
	
	@PostMapping
	public RedirectView signup(Authentication authentication ,MemberDTO memberDTO) {
    	CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		MemberDTO signupMemberDTO = new MemberDTO(oAuth2User.getAttributes());
		signupMemberDTO.setId(authentication.getName());
		signupMemberDTO.setName(memberDTO.getName());
		signupMemberDTO.setBio(memberDTO.getBio());
		signupMemberDTO.setEmail(memberDTO.getEmail());
		signupMemberDTO = memberService.addMember(signupMemberDTO);
		return new RedirectView("/journal/"+signupMemberDTO.getId());
	}
}

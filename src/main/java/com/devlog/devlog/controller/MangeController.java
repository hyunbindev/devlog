package com.devlog.devlog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/manage")
@RequiredArgsConstructor
public class MangeController {
	private final PostService postService;
	private final MemberService memberService;

	@GetMapping("/post")
	public String createPostView(Authentication authentication) {
		return "/thymeleaf/createpost";
	}
	
	@PostMapping("/post")
	public String createPost(Authentication authentication , @RequestParam("title")String title, @RequestParam("text")String text) {
		PostDTO postDTO = new PostDTO();
		MemberDTO writerDTO = new MemberDTO();
		writerDTO.setId(authentication.getName());
		writerDTO = memberService.getMember(writerDTO);
		
		postDTO.setWriter(writerDTO);
		postDTO.setTitle(title);
		postDTO.setText(text);
		
		postDTO = postService.addPost(postDTO);
		return "redirect:/journal/"+writerDTO.getId()+"/"+postDTO.getId();
	}
}
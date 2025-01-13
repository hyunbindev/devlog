package com.devlog.devlog.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devlog.devlog.aspect.annotation.CheckOwner;
import com.devlog.devlog.aspect.annotation.TestAspect;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalController {
	private final MemberService memberService;
	private final PostService postService;
	
	@CheckOwner
	@GetMapping("/{memberId}")
	public String getMemberIndex(Authentication authentication , @PathVariable("memberId")String id ,Model model) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(id);
		memberDTO = memberService.getMember(memberDTO);
    	model.addAttribute("memberDTO", memberDTO);
    	List<PostDTO>postDTOs = postService.getPostList(memberDTO);
    	model.addAttribute("postDTOs",postDTOs);
        return "thymeleaf/main";
	}
	
	@CheckOwner
	@GetMapping("/{memberId}/{postId}")
	public String getPost(Authentication authentication , @PathVariable("memberId")String memberId ,Model model ,@PathVariable("postId")Long postId) {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(postId);
		postDTO = postService.getPost(postDTO);
		MemberDTO writerDTO = memberService.getMember(postDTO.getWriter());
		log.error(writerDTO.getId());
		model.addAttribute("memberDTO",writerDTO);
		model.addAttribute("postDTO",postDTO);
		
		return "thymeleaf/post";
	}
}
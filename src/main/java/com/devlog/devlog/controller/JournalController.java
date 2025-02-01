package com.devlog.devlog.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devlog.devlog.aspect.annotation.CheckOwner;
import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.service.BoardService;
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
	private final BoardService boardService;
	@GetMapping("/{memberId}")
	public String getMemberIndex(Authentication authentication , @PathVariable("memberId")String id ,Model model) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(id);
		memberDTO = memberService.getMember(memberDTO);
    	
    	Page<PostDTO>postPage = postService.getAllPostList(memberDTO , 0);
    	List<PostDTO>postDTOs = postPage.getContent();
    	List<BoardDTO>boardDTOs = boardService.getAllBoard(memberDTO);
    	
    	model.addAttribute("memberDTO", memberDTO);
    	model.addAttribute("boardDTOs",boardDTOs);
    	model.addAttribute("postDTOs",postDTOs);
    	
        return "thymeleaf/journal/main";
	}
	
	@GetMapping("/{memberId}/{postId}")
	public String getPost(Authentication authentication , @PathVariable("memberId")String memberId ,Model model ,@PathVariable("postId")Long postId) {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(postId);

		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(memberId);
		
		postDTO = postService.getPost(postDTO , memberDTO);
		MemberDTO writerDTO = memberService.getMember(memberDTO);
		
		BoardDTO boardDTO = postDTO.getBoard();
		
		List<BoardDTO>boardDTOs = boardService.getAllBoard(memberDTO);
		
		model.addAttribute("memberDTO",writerDTO);
		model.addAttribute("postDTO",postDTO);
		model.addAttribute("boardDTO",boardDTO);
		model.addAttribute("boardDTOs", boardDTOs);
		return "thymeleaf/journal/post";
	}
}
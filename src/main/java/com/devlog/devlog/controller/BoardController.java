package com.devlog.devlog.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class BoardController {
	private final BoardService boardService;
	private final MemberService memberService;
	private final PostService postService;
	@GetMapping
	public String getAllBoardView(Authentication authentication){
		return "thymeleaf/manage/main";
	}
	
	@GetMapping("/{memberId}/board/{board}")
	public String getBoardView(
			Authentication authentication ,
			@PathVariable("memberId")String memberId, 
			Model model ,
			@PathVariable("board")String boardTitle,
			@RequestParam(name ="page" ,defaultValue = "0") int page)
	{
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setTitle(boardTitle);
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(memberId);
		
		memberDTO = memberService.getMember(memberDTO);
		
		boardDTO = boardService.getBoard(memberDTO, boardDTO);
		
		Page<PostDTO> postPage = postService.getPostListByBoard(memberDTO, boardDTO ,page);
		List<PostDTO> postDTOs = postPage.getContent();
		List<BoardDTO>boardDTOs = boardService.getAllBoard(memberDTO);
    	model.addAttribute("boardDTOs",boardDTOs);
		model.addAttribute("boardDTO",boardDTO);
		model.addAttribute("memberDTO",memberDTO);
		model.addAttribute("postDTOs",postDTOs);
		return "thymeleaf/journal/board";
	}
}

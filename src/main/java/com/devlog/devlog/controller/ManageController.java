package com.devlog.devlog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.MemberService;
import com.devlog.devlog.service.PostService;
import com.devlog.devlog.service.UserTrackingService.UserTrackingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/manage")
@RequiredArgsConstructor
public class ManageController {
	private final PostService postService;
	private final MemberService memberService;
	private final BoardService boardService;
	private final UserTrackingService userTrackingService;
	//관리자 페이지
	@GetMapping
	public String getManageView(Authentication authentication , Model model){
		
		return "thymeleaf/manage/main";
	}
	//게시판 조회
	@GetMapping("/board")
	public String getManageBoardView(Authentication authentication , Model model) {

		return "thymeleaf/manage/board";
	}
	//포스트 조회
	@GetMapping("/post")
	public String getManagePostView(Authentication authentication , Model model) {
		MemberDTO memberDTO = new MemberDTO(authentication.getName());
		List<BoardDTO> boardDTOs = boardService.getAllBoard(memberDTO);
		model.addAttribute("boardDTOs", boardDTOs);
		return "thymeleaf/manage/post";
	}
	@GetMapping("/statistics")
	public String getStatisticsView(Authentication authentication , Model model) {
		String userId = authentication.getName();
		model.addAttribute("today_visitor_count", userTrackingService.getJournalVisitorCount(userId));
		return "thymeleaf/manage/statistics";
	}
	//새 포스트 작성
	@GetMapping("/newpost")
	public String createPostView(Authentication authentication , Model model) {
		MemberDTO writerDTO = new MemberDTO();
		writerDTO.setId(authentication.getName());
		
		List<BoardDTO> boardDTOs = boardService.getAllBoard(writerDTO);
		model.addAttribute("boardDTOs",boardDTOs);
		return "thymeleaf/createpost";
	}
	//포스트 수정
	@GetMapping("/updatepost/{postId}")
	public String updatePostView(Authentication authentication , Model model ,@PathVariable("postId")Long postId) {
		MemberDTO writerDTO = new MemberDTO(authentication.getName());
		PostDTO postDTO = new PostDTO(postId);
		
		postDTO = postService.getPost(postDTO, writerDTO);
		List<BoardDTO> boardDTOs = boardService.getAllBoard(writerDTO);
		model.addAttribute("post", postDTO);
		model.addAttribute("boardDTOs",boardDTOs);
		return "thymeleaf/updatepost";
	}
	//새로운 포스트 입력값받기
	@PostMapping("/newpost")
	@ResponseBody
	public ResponseEntity<Map> createPost(Authentication authentication , @RequestBody PostDTO postDTO) {
		MemberDTO writerDTO = new MemberDTO(authentication.getName());
		
		writerDTO = memberService.getMember(writerDTO);
		
		postDTO.setWriter(writerDTO);
		
		
		postDTO = postService.addPost(postDTO ,writerDTO);
		
		return ResponseEntity.ok(Map.of("redirect", "/journal/" + writerDTO.getId() + "/" + postDTO.getId()));
	}
	@PostMapping("/updatepost")
	@ResponseBody
	public ResponseEntity<Map> updatePost(Authentication authentication , @RequestBody PostDTO postDTO) {
		MemberDTO writerDTO = new MemberDTO(authentication.getName());
		postService.updatePost(postDTO, writerDTO);
		return ResponseEntity.ok(Map.of("redirect", "/journal/" + writerDTO.getId() + "/" + postDTO.getId()));
	}
}
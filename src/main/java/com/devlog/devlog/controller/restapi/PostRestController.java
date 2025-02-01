package com.devlog.devlog.controller.restapi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.service.BoardService;
import com.devlog.devlog.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostRestController {
	private final PostService postService;
	@PostMapping("/delete")
	public ResponseEntity<String> deletePost(Authentication authentication ,@RequestBody PostDTO deletePost) {
		MemberDTO writerDTO = new MemberDTO();
		writerDTO.setId(authentication.getName());
		postService.deletePost(writerDTO, deletePost);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	@GetMapping
	public ResponseEntity<Page<PostDTO>> getPost(
			Authentication authentication, 
			@RequestParam(name="board" ,required = false) Long board, 
			@RequestParam(name="keyword" ,required = false) String keyword,
			@RequestParam(name="page" ,defaultValue = "0") int page)
	{
		MemberDTO memberDTO = new MemberDTO(authentication.getName());
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(board);
		//게시판및 키워드기반조회
		if(board != null && keyword != null) {
			Page<PostDTO> postPage = postService.getPostListByBoardAndKeyworrd(memberDTO, boardDTO, keyword, page);
			return new ResponseEntity<Page<PostDTO>>(postPage, HttpStatus.OK);
		}
		//게시판 기준 조회
		if(board != null) {
			Page<PostDTO> postPage = postService.getPostListByBoard(memberDTO, boardDTO, page);
			return new ResponseEntity<Page<PostDTO>>(postPage, HttpStatus.OK);
		}
		//키워드 기준 조회
		if(keyword != null) {
			Page<PostDTO> postPage = postService.getPostListByKeyword(memberDTO, keyword, page);
			return new ResponseEntity<Page<PostDTO>>(postPage, HttpStatus.OK);
		}
		//전부 조회
		Page<PostDTO> postDTOs = postService.getAllPostList(memberDTO, page);
		return new ResponseEntity<Page<PostDTO>>(postDTOs, HttpStatus.OK);
	}
}

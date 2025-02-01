package com.devlog.devlog.controller.restapi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devlog.devlog.controller.JournalController;
import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardRestController {
	private final BoardService boardService; 
	@PostMapping
	public ResponseEntity<List<BoardDTO>> addBoard(Authentication authentication ,@RequestBody BoardDTO newBoard) {
		 BoardDTO boardDTO = newBoard;
		 MemberDTO memberDTO = new MemberDTO();
		 memberDTO.setId(authentication.getName());
		 boardDTO.setMemberDTO(memberDTO);
		 boardDTO.setTitle(boardDTO.getTitle());
		 boardDTO = boardService.addBoard(memberDTO,boardDTO);
		 List<BoardDTO> boardDTOs = boardService.getAllBoard(memberDTO);
		 return new ResponseEntity<List<BoardDTO>>(boardDTOs , HttpStatus.CREATED);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<List<BoardDTO>> deleteBoard(Authentication authentication ,@RequestBody BoardDTO deleteBoard) {
		BoardDTO deleteBoardDTO = deleteBoard;
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(authentication.getName());
		deleteBoardDTO.setMemberDTO(memberDTO);
		boardService.deleteBoard(memberDTO, deleteBoardDTO);
		
		List<BoardDTO> boardDTOs = boardService.getAllBoard(memberDTO);
		
		return new ResponseEntity<List<BoardDTO>>(boardDTOs, HttpStatus.ACCEPTED);
	}
	@GetMapping
	public ResponseEntity<List<BoardDTO>> getAllBoardList(Authentication authentication){
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(authentication.getName());
		List<BoardDTO> boardDTOs = boardService.getAllBoard(memberDTO);
		return new ResponseEntity<List<BoardDTO>>(boardDTOs , HttpStatus.OK);
	}
}

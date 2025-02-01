package com.devlog.devlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
import com.devlog.devlog.controller.BoardController;
import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.domain.BoardEntity;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.exception.JournalException;
import com.devlog.devlog.repository.BoardRepository;
import com.devlog.devlog.repository.MemberRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService{
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	
	private final BoardService boardService;
	@Transactional(readOnly = true)
	public MemberDTO getMember(MemberDTO dto) {
		String memberId = dto.getId();
		MemberDTO memberDTO = new MemberDTO();
		Optional<MemberEntity> optionalEntity = memberRepository.findById(memberId);
		if(optionalEntity.isEmpty()) {
			memberDTO.setMember(false);
			return memberDTO;
		}
		memberDTO = entityToDTO(optionalEntity.get());
		memberDTO.setMember(true);
		return memberDTO;
	}
	@Transactional
	public MemberDTO addMember(MemberDTO memberDTO) {
		Optional<MemberEntity> optionalEntity = memberRepository.findById(memberDTO.getId());
		if(optionalEntity.isEmpty()) {
			MemberEntity memberEntity = dtoToEntity(memberDTO);
			memberEntity = memberRepository.save(memberEntity);
			
			BoardEntity defaultBoardEntity = new BoardEntity();
			defaultBoardEntity.setMember(memberEntity);
			defaultBoardEntity.setTitle("기본게시판");
			defaultBoardEntity = boardRepository.save(defaultBoardEntity);
			return entityToDTO(memberEntity);
		}
		//계정 존재시 중복 회원가입 방지
		throw new RuntimeException();
	}
	
	public MemberEntity dtoToEntity(MemberDTO dto) {
		MemberEntity entity = new MemberEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setBio(dto.getBio());
		entity.setAvatar_url(dto.getAvatar_url());
		entity.setRepos_url(dto.getRepos_url());
		entity.setEmail(dto.getEmail());
		return entity;
	}
	
	public MemberDTO entityToDTO(MemberEntity entity) {
		MemberDTO dto = new MemberDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setBio(entity.getBio());
		dto.setAvatar_url(entity.getAvatar_url());
		dto.setEmail(entity.getEmail());
		dto.setRepos_url(entity.getRepos_url());
		return dto;
	}
}

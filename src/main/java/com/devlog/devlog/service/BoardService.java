package com.devlog.devlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.domain.BoardEntity;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.exception.JournalException;
import com.devlog.devlog.repository.BoardRepository;
import com.devlog.devlog.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
	private final BoardRepository boardRepository;
	
	private final MemberRepository memberRepository;
	
	//계시판 추가
	@Transactional
	public BoardDTO addBoard(MemberDTO memberDTO , BoardDTO boardDTO) {
		Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
		//요청자 유저 정보 확인
		if(optionalMemberEntity.isEmpty()) {
			throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
		}
		
		MemberEntity memberEntity = optionalMemberEntity.get();
		Optional<BoardEntity> optionalBoardEntity = boardRepository.findByTitleAndMember(boardDTO.getTitle(), memberEntity);
		//동일 게시판 존재시
		if(optionalBoardEntity.isPresent()) {
			//throw error처리
			//수정 예정
			throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
		}
		
		BoardEntity boardEntity = new BoardEntity();
		
		boardEntity.setMember(memberEntity);
		boardEntity.setTitle(boardDTO.getTitle());
		boardEntity = boardRepository.save(boardEntity);
		
		boardDTO = entityToDTO(boardEntity);
		return boardDTO;
	}
	
	
	//사용자 계시판 가져오기
	@Transactional(readOnly = true)
	public List<BoardDTO> getAllBoard(MemberDTO memberDTO){
		Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
		List<BoardDTO> boardDTOs = new ArrayList<>();
		if(optionalMemberEntity.isPresent()) {
			MemberEntity memberEntity = optionalMemberEntity.get();
			List<BoardEntity> boardEntitys = boardRepository.findByMember(memberEntity);
			for(BoardEntity entity : boardEntitys) {
				BoardDTO dto = entityToDTO(entity);
				boardDTOs.add(dto);
			}
		}
		return boardDTOs;
	}
	//게시판 삭제
	@Transactional
	public void deleteBoard(MemberDTO memberDTO , BoardDTO boardDTO) {
		Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
		Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDTO.getId());
		//요청자 존재 여부 확인
		if(optionalMemberEntity.isEmpty()) {
			throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
		}
		//보드 존재 여부 확인
		if(optionalBoardEntity.isEmpty()) {
			throw new JournalException(JournalExceptionConst.BOARD_NOT_FOUND);
		}
		MemberEntity memberEntity = optionalMemberEntity.get();
		BoardEntity boardEntity = optionalBoardEntity.get();
		
		//요청자와 계시판의 소유가 맞지 않으면
		if(boardEntity.getMember().getId().equals(memberEntity.getId())) {
			//권환없음 예외처리
		}
		
		//계시판의 소유 포스트 확인
		if(boardEntity.getPost().size() >0) {
			//보드가 포스트를 소유 할경우 삭제 불가 예외처리
			throw new JournalException(JournalExceptionConst.BOARD_NOT_FOUND);
		}
		//최종 보드 삭제
		boardRepository.delete(boardEntity);
	}
	
	//특정 게시판 조회
	@Transactional(readOnly = true)
	public BoardDTO getBoard(MemberDTO memberDTO,BoardDTO boardDTO) {
		Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
		//맴버가 존지하지 않음 예외처리
		if(optionalMemberEntity.isEmpty()) {
			throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
		}
		MemberEntity memberEntity = optionalMemberEntity.get();
		Optional<BoardEntity> optionalBoardEntity = boardRepository.findByTitleAndMember(boardDTO.getTitle(), memberEntity);
		//보드가 존재하지 않을 시 예외처리
		if(optionalBoardEntity.isEmpty()) {
			//throw error
			throw new JournalException(JournalExceptionConst.BOARD_NOT_FOUND);
		}
		return entityToDTO(optionalBoardEntity.get());
	}
	
	
	//boardEntity to DTO Mapper
	public BoardDTO entityToDTO (BoardEntity entity) {
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(entity.getId());
		boardDTO.setTitle(entity.getTitle());
		boardDTO.setPostCount(entity.getPost().size());
		return boardDTO;
	}
	//board dto to entity
	public BoardEntity dtoToEntity (BoardDTO dto) {
		BoardEntity entity = new BoardEntity();
		entity.setId(dto.getId());
		entity.setTitle(dto.getTitle());
		return entity;
	}
}

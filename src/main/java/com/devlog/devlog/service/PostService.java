package com.devlog.devlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlog.devlog.constants.exception.JournalExceptionConst;
import com.devlog.devlog.data.dto.BoardDTO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.domain.BoardEntity;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.PostEntity;
import com.devlog.devlog.exception.JournalException;
import com.devlog.devlog.repository.BoardRepository;
import com.devlog.devlog.repository.MemberRepository;
import com.devlog.devlog.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final MemberRepository memberRepository;
	private final MemberService memberSerivce;
	private final PostRepository postRepository;
	private final BoardService boardService;
	private final BoardRepository boardRepository;
	
	private final PageService pageService;
	//포스트 리스트 조회
	//추후 최적화를위해 필요한 데이터만 넣을 예정
	@Transactional(readOnly = true)
	public Page<PostDTO> getAllPostList(MemberDTO memberDTO , int page){
		//사용자 찾을 수 없을때 예외처리
		MemberEntity memberEntity = memberRepository.findById(memberDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		
		//default Pageable 생성
		Pageable pageable = pageService.getDefaultPageable(page);
		
		Page<PostEntity> postPage = postRepository.findByWriterOrderByIdDesc(memberEntity,pageable);
		//DTO변환
		List<PostDTO> postDTOs = new ArrayList<>();
		for(PostEntity postEntity :postPage.getContent()) {
			PostDTO postDTO = entityToDTO(postEntity);
			postDTOs.add(postDTO);
		}
		return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
	}
	
	//단일 포스트 조회
	@Transactional(readOnly = true)
	public PostDTO getPost(PostDTO postDTO , MemberDTO memberDTO) {
		Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
		Optional<PostEntity> optionalPostEntity = postRepository.findById(postDTO.getId());
		//사용자와 포스트 존재 예외처리
		if(optionalMemberEntity.isEmpty()) {
			//사용자 존재 예외 처리
			throw new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND);
		}
		if(optionalPostEntity.isEmpty()) {
			//포스트 존재 예외처리
			throw new JournalException(JournalExceptionConst.POST_NOT_FOUND);
		}
		MemberEntity memberEntity = optionalMemberEntity.get();
		PostEntity postEntity = optionalPostEntity.get();
		//요청한 포스트와 유저 관계가 맞지 않을 경우
		if(!memberEntity.getId().equals(postEntity.getWriter().getId())) {
			throw new JournalException(JournalExceptionConst.POST_NOT_FOUND);
		}
		return entityToDTO(postEntity);
	}
	
	//포스트 추가
	@Transactional
	public PostDTO addPost(PostDTO newPostDTO, MemberDTO writerDTO) {
		//사용자 검증 
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		//보드 존재 검증
		BoardEntity boardEntity= boardRepository.findById(newPostDTO.getBoard().getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.BOARD_NOT_FOUND));
		
		
		PostEntity newPostEntity = dtoToEntity(newPostDTO);
		newPostEntity.setWriter(writerEntity);
		newPostEntity.setBoard(boardEntity);
		newPostEntity = postRepository.save(newPostEntity);
		
		boardEntity.getPost().add(newPostEntity);
		boardRepository.save(boardEntity);
		
		return entityToDTO(newPostEntity);
	}
	//게시판으로 포스트 조회
	@Transactional(readOnly = true)
	public Page<PostDTO> getPostListByBoard(MemberDTO writerDTO , BoardDTO boardDTO ,int page){
		//작성자 존재 검증 및 예외 처리
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));

		//게시판 존재 검증 및 예외처리
		BoardEntity boardEntity = boardRepository.findByTitleAndMember(boardDTO.getTitle(),writerEntity)
				.orElseGet(() -> boardRepository.findById(boardDTO.getId())
					.orElseThrow(()->new JournalException(JournalExceptionConst.BOARD_NOT_FOUND)));
		
		Pageable pageable = pageService.getDefaultPageable(page);
		
		Page<PostEntity> postPage = postRepository.findByWriterAndBoardOrderByIdDesc(writerEntity, boardEntity ,pageable);
		
		List<PostDTO> postDTOs = new ArrayList<>();
		
		for(PostEntity entity : postPage.getContent()) {
			postDTOs.add(entityToDTO(entity));
		}
		return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
	}
	
	//키워드 기반 게시글 조회
	@Transactional(readOnly = true)
	public Page<PostDTO> getPostListByKeyword(MemberDTO writerDTO, String KeyWord,int page){
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		
		Pageable pageable = pageService.getDefaultPageable(page);
		
		Page<PostEntity> postPage = postRepository.findByWriterAndTitleContaining(writerEntity, KeyWord, pageable);
		
		List<PostDTO> postDTOs = new ArrayList<>();
		
		for(PostEntity entity : postPage.getContent()) {
			postDTOs.add(entityToDTO(entity));
		}
		
		return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
	}
	
	//키워드와 보드 기반 게시글 조회
	@Transactional(readOnly = true)
	public Page<PostDTO> getPostListByBoardAndKeyworrd(MemberDTO writerDTO , BoardDTO boardDTO , String keyword,int page){
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		BoardEntity boardEntity = boardRepository.findByTitleAndMember(boardDTO.getTitle(),writerEntity)
				.orElseGet(() -> boardRepository.findById(boardDTO.getId())
					.orElseThrow(()->new JournalException(JournalExceptionConst.BOARD_NOT_FOUND)));
		
		Pageable pageable = pageService.getDefaultPageable(page);
		
		Page<PostEntity> postPage = postRepository.findByWriterAndBoardAndTitleContaining(writerEntity, boardEntity, keyword, pageable);
		
		List<PostDTO> postDTOs = new ArrayList<>();
		
		for(PostEntity entity : postPage.getContent()) {
			postDTOs.add(entityToDTO(entity));
		}
		
		return new PageImpl<>(postDTOs, pageable, postPage.getTotalElements());
	}
	
	//포스트 삭제
	@Transactional
	public void deletePost(MemberDTO writerDTO , PostDTO postDTO) {
		//작성자 존재 검증 및 예외 처리
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()-> new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		//게시판 존재 검증 및 예외처리
		PostEntity postEntity = postRepository.findById(postDTO.getId())
				.orElseThrow(()-> new JournalException(JournalExceptionConst.POST_NOT_FOUND));
		//작성자와 요청자가 다름
		if(!writerEntity.getId().equals(postEntity.getWriter().getId())) {
			//권환없음 예외
			return;
		}
		
		BoardEntity boardEntity = postEntity.getBoard();
		boardEntity.getPost().removeIf(post -> postEntity.getId().equals(post.getId()));
		//연관관계삭제
		boardRepository.save(boardEntity);
		postRepository.delete(postEntity);
	}
	
	@Transactional
	public void updatePost(PostDTO postDTO, MemberDTO writerDTO) {
		//사용자 검증 
		MemberEntity writerEntity = memberRepository.findById(writerDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.JOURNAL_NOT_FOUND));
		//포스트 존재 검증
		PostEntity postEntity= postRepository.findById(postDTO.getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.POST_NOT_FOUND));
		BoardEntity boardEntity = boardRepository.findById(postDTO.getBoard().getId())
				.orElseThrow(()->new JournalException(JournalExceptionConst.BOARD_NOT_FOUND));
		//사용자가 포스트 소유 검증
		if(!writerEntity.getId().equals(postEntity.getWriter().getId())) {
			//권환없음 throw
		}
		postEntity.setTitle(postDTO.getTitle());
		postEntity.setBoard(boardEntity);
		postEntity.setText(postDTO.getText());
		
		postRepository.save(postEntity);
	}
	public PostDTO entityToDTO(PostEntity entity) {
		PostDTO dto = new PostDTO();
		dto.setId(entity.getId());
		
		MemberDTO writer = memberSerivce.entityToDTO(entity.getWriter());
		dto.setWriter(writer);
		
		BoardDTO board = boardService.entityToDTO(entity.getBoard());
		
		dto.setBoard(board);
		dto.setTitle(entity.getTitle());
		dto.setText(entity.getText());
		dto.setThumbText(entity.getThumbText());
		dto.setCreateDate(entity.getCreateDate());
		return dto;
	}
	
	public PostEntity dtoToEntity(PostDTO dto) {
		PostEntity postEntity = new PostEntity();
		MemberEntity wirterEntity = memberSerivce.dtoToEntity(dto.getWriter());
		postEntity.setWriter(wirterEntity);
		postEntity.setId(dto.getId());
		postEntity.setTitle(dto.getTitle());
		postEntity.setText(dto.getText());
		postEntity.setThumbText(dto.getThumbText());
		return postEntity;
	}
}

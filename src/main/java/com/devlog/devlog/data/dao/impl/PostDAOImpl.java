package com.devlog.devlog.data.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.devlog.devlog.data.dao.CommonDAO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.PostEntity;
import com.devlog.devlog.repository.MemberRepository;
import com.devlog.devlog.repository.PostRepository;
import com.devlog.devlog.service.MemberService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostDAOImpl implements CommonDAO<PostEntity,PostDTO>{
	private final MemberDAOImpl memberDAOImpl;
	private final PostRepository postRepository;
	
	public PostDTO addPost(PostDTO dto) {
		PostEntity postEntity = dtoToEntity(dto);
		postEntity = postRepository.save(postEntity);
		return entityToDTO(postEntity);
	}
	
	public PostDTO getPost(PostDTO dto) {
		Optional<PostEntity> postEntity = postRepository.findById(dto.getId());
		if(postEntity.isEmpty()) return null;
		return entityToDTO(postEntity.get());
	}
	
	@Override
	public PostDTO entityToDTO(PostEntity entity) {
		PostDTO dto = new PostDTO();
		dto.setId(entity.getId());
		MemberDTO writer = memberDAOImpl.entityToDTO(entity.getWriter());
		dto.setWriter(writer);
		dto.setTitle(entity.getTitle());
		dto.setText(entity.getText());
		dto.setCreateDate(entity.getCreateDate());
		return dto;
	}
	
	@Override
	public PostEntity dtoToEntity(PostDTO dto) {
		PostEntity postEntity = new PostEntity();
		MemberEntity wirterEntity = memberDAOImpl.getMemberEntity(dto.getWriter());
		postEntity.setWriter(wirterEntity);
		postEntity.setId(dto.getId());
		postEntity.setTitle(dto.getTitle());
		postEntity.setText(dto.getText());
		return postEntity;
	}
}

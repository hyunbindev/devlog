package com.devlog.devlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devlog.devlog.data.dao.impl.PostDAOImpl;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.data.dto.PostDTO;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.PostEntity;
import com.devlog.devlog.repository.MemberRepository;
import com.devlog.devlog.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostDAOImpl postDAOImpl;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	public PostDTO addPost(PostDTO dto) {
		return postDAOImpl.addPost(dto);
	}
	public PostDTO getPost(PostDTO dto) {
		return postDAOImpl.getPost(dto);
	}
	
	public List<PostDTO> getPostList(MemberDTO memberDTO){
		Optional<MemberEntity> memberEntity = memberRepository.findById(memberDTO.getId());
		List<PostEntity> postEntitys = postRepository.findByWriter(memberEntity.get());
		List<PostDTO> postDTOs = new ArrayList<>();
		for(PostEntity postEntity : postEntitys) {
			postDTOs.add(postDAOImpl.entityToDTO(postEntity));
		}
		return postDTOs;
	}
}

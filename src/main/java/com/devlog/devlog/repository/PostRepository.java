package com.devlog.devlog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devlog.devlog.domain.BoardEntity;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity,Long>{
	
	Page<PostEntity> findByWriterOrderByIdDesc(MemberEntity writer ,Pageable pageable);
	
	Page<PostEntity> findByWriterAndBoardOrderByIdDesc(MemberEntity writer ,BoardEntity board ,Pageable pageable);
	
	Page<PostEntity> findByWriterAndTitleContaining(MemberEntity writer ,String keyword ,Pageable pagealbe);
	
	Page<PostEntity> findByWriterAndBoardAndTitleContaining(MemberEntity writer,BoardEntity board ,String keyword ,Pageable pagealbe);
}
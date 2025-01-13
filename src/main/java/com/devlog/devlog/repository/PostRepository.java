package com.devlog.devlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity,Long>{
	List<PostEntity> findByWriter(MemberEntity writer);
}
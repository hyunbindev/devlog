package com.devlog.devlog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devlog.devlog.domain.BoardEntity;
import com.devlog.devlog.domain.MemberEntity;

public interface BoardRepository extends JpaRepository<BoardEntity,Long>{
	List<BoardEntity> findByMember(MemberEntity memberEntity);
	Optional<BoardEntity> findByTitleAndMember(String title, MemberEntity memberEntity);
}

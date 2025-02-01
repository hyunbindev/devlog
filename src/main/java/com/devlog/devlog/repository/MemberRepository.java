package com.devlog.devlog.repository;

import com.devlog.devlog.domain.MemberEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,String> {
	Optional<MemberEntity> findById(String id);
}

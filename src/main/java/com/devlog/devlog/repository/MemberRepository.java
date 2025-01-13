package com.devlog.devlog.repository;

import com.devlog.devlog.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,String> {

}

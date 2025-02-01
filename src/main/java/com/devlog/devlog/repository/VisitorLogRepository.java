package com.devlog.devlog.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.domain.VisitorLogEntity;

public interface VisitorLogRepository extends JpaRepository<VisitorLogEntity,Long>{
	int countBymember(MemberEntity memberEntity);
	//
	List<VisitorLogEntity> findByvisitDateBetweenAndMember(LocalDate startDate , LocalDate endDate , MemberEntity member);
}

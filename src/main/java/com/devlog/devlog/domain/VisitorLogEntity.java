package com.devlog.devlog.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "visitor_log")
public class VisitorLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="visitor_log_id")
    private Long id;
    @ManyToOne
	private MemberEntity member;
	
    @CreationTimestamp
    private LocalDate visitDate;
    
    private String referrer;
    public VisitorLogEntity(MemberEntity memberEntity) {
    	this.member= memberEntity;
    }
    public VisitorLogEntity() {
    }
}

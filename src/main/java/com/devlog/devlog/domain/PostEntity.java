package com.devlog.devlog.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;

import com.devlog.devlog.data.dto.MemberDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="writer_id")
    private MemberEntity writer;
    
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = true)
    private BoardEntity board;
    
    @Column(nullable = false)
    private String title;
    
    @Lob
    @Column(nullable = false)
    private String text;
    
    private String thumbText;
    
    @CreationTimestamp
	private LocalDateTime createDate;
}

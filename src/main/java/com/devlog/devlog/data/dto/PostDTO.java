package com.devlog.devlog.data.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
	private Long id;
	private MemberDTO writer;
	private String title;
	private BoardDTO board;
	private String text;
	private String thumbText;
	private LocalDateTime createDate;
	
	public PostDTO() {
		
	}
	
	public PostDTO(Long postId) {
		this.id = postId;
	}
}
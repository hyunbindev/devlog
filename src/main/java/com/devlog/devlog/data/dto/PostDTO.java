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
	private String text;
	private LocalDateTime createDate;
}
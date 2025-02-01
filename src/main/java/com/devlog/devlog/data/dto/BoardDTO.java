package com.devlog.devlog.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
	private MemberDTO memberDTO;
	private Long id;
	private String title;
	private int postCount;
}

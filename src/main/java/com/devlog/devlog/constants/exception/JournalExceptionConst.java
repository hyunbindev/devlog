package com.devlog.devlog.constants.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JournalExceptionConst {
	JOURNAL_NOT_FOUND(404,"사용자가 존재하지 않습니다."),
	POST_NOT_FOUND(404,"포스트가 존재하지 않습니다."),
	BOARD_NOT_FOUND(404,"게시판이 존재하지 않습니다");
	private final int status;
	private final String msg;
}

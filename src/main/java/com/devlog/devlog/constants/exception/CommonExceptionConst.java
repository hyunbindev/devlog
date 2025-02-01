package com.devlog.devlog.constants.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonExceptionConst {
	BAD_REQUEST(400,"잘못된 요청");
	private final int status;
	private final String msg;
}

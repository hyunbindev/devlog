package com.devlog.devlog.exception;

import com.devlog.devlog.constants.exception.JournalExceptionConst;

public class JournalException extends RuntimeException{
	private JournalExceptionConst excpetionConst;
	
	public JournalException(JournalExceptionConst exceptionConst) {
		this.excpetionConst = exceptionConst;
	}
}
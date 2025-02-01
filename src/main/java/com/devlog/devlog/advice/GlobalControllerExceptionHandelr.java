package com.devlog.devlog.advice;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.view.RedirectView;

import com.devlog.devlog.exception.JournalException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandelr {
	@ExceptionHandler(JournalException.class)
	@ResponseBody
	public String hanldeNotFoundException(JournalException ex) {
		return "notfound exception";
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	public RedirectView hanldeNotFoundException(NoResourceFoundException ex) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("");
		return redirectView;
	}
}

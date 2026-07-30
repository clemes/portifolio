package com.prj.springai.exceptions;

import java.nio.channels.ClosedChannelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.prj.springai.poetry.PoetryController;

@ControllerAdvice
public class MyGlobalExceptionHandler extends ResponseEntityExceptionHandler{
	private static final Logger log = LogManager.getLogger(PoetryController.class);

	private static final String LLM_COMMUNICATION_ERROR =
		    "Unable to communicate with the configured LLM. Please try again later.";

	@ExceptionHandler(ClosedChannelException.class)
	ProblemDetail handle(ClosedChannelException exception) {
		log.error("OpenAI server is not accessible.", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, LLM_COMMUNICATION_ERROR);
	}
}

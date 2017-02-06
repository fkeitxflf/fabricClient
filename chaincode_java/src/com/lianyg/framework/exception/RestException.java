package com.lianyg.framework.exception;

/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/

import org.springframework.http.HttpStatus;

/**
 * 专用于Restful Service的异常.
 * 
 */
public class RestException extends RuntimeException {

	private static final long serialVersionUID = 2425035218872212101L;

	public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	public RestException() {
	}

	public RestException(HttpStatus status) {
		this.status = status;
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}

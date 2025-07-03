package com.zayaanit.exception;

import org.springframework.http.HttpStatus;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 23, 2025
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 6837468368181257192L;

	private final HttpStatus status;

	public CustomException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}

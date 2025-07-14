package com.zayaanit.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 23, 2025
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
		ErrorResponse error = ErrorResponse.builder()
				.status(ex.getStatus().value())
				.error(ex.getStatus().getReasonPhrase())
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<>(error, ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message(ex.getMessage())
				.build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errorMessages = new StringBuilder();
		Map<String, String> globalErrors = new HashMap<>();
		Map<String, String> fieldErrors = new HashMap<>();

		// Field-level errors
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			fieldErrors.put(error.getField(), error.getDefaultMessage());
			errorMessages.append(error.getDefaultMessage() + " | ");
		});

		// Class-level (global) errors like @StartBeforeEndTime
		ex.getBindingResult().getGlobalErrors().forEach(error -> {
			// Use "global" as a key, or error.getObjectName() if needed
			globalErrors.put("global", error.getDefaultMessage());
			errorMessages.append(error.getDefaultMessage()).append(" | ");
		});

		String message = errorMessages.toString();
		if (message.endsWith(" | ")) {
			message = message.substring(0, message.length() - 3);
		}

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.fieldErrors(fieldErrors)
				.globalErrors(globalErrors)
				.message(message)
				.build();
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}

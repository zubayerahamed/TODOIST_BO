package com.zayaanit.model;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.zayaanit.enums.ResponseStatusType;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 25, 2025
 */
public class ResponseBuilder {
	public static <T> ResponseEntity<SuccessResponse<T>> build(ResponseStatusType type, T data) {
		SuccessResponse<T> response = SuccessResponse.<T>builder()
				.status(type.getHttpStatus().value())
				.message(type.getMessage())
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, type.getHttpStatus());
	}

	// Optional: builder with custom message override
	public static <T> ResponseEntity<SuccessResponse<T>> build(ResponseStatusType type, String customMessage, T data) {
		SuccessResponse<T> response = SuccessResponse.<T>builder()
				.status(type.getHttpStatus().value())
				.message(customMessage)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, type.getHttpStatus());
	}
}

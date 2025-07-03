package com.zayaanit.todoist.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 23, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private int status;
	private String error;
	private String message;
	Map<String, String> fieldErrors;
}

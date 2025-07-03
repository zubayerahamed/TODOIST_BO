package com.zayaanit.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 25, 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse<T> {
	private int status; 				// e.g., 200, 201
	private String message; 			// e.g., "Data fetched successfully"
	private T data; 					// Can be List, Object, Map, etc.
	private LocalDateTime timestamp;
}

package com.zayaanit.todoist.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 25, 2025
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN.value()).error("Forbidden")
				.message("You do not have permission to access this resource")
				.timestamp(LocalDateTime.now())
				.build();

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");
		mapper.writeValue(response.getWriter(), error);
	}
}

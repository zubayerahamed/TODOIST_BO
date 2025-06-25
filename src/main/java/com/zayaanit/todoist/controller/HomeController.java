package com.zayaanit.todoist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zayaanit.todoist.enums.ResponseStatusType;
import com.zayaanit.todoist.model.ResponseBuilder;
import com.zayaanit.todoist.model.SuccessResponse;

/**
 * Zubayer Ahamed
 * @since Jun 17, 2025
 */
@RestController
@RequestMapping("/home")
public class HomeController {

	@GetMapping
	public ResponseEntity<SuccessResponse<String>> index() {
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, "Hellow from secured endpoint");
	}
}

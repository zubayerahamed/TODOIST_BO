package com.zayaanit.todoist.module;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.todoist.enums.ResponseStatusType;
import com.zayaanit.todoist.model.ResponseBuilder;
import com.zayaanit.todoist.model.SuccessResponse;

/**
 * Zubayer Ahamed
 * @since Jun 17, 2025
 */
@RestApiController
@RequestMapping("/api/v1/home")
public class HomeController {

	@GetMapping
	public ResponseEntity<SuccessResponse<String>> index() {
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, "Hellow from secured endpoint");
	}
}

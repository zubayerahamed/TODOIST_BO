package com.zayaanit.todoist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Zubayer Ahamed
 * @since Jun 17, 2025
 */
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@GetMapping
	public ResponseEntity<String> index() {
		return ResponseEntity.ok("Hellow from secured endpoint");
	}
}

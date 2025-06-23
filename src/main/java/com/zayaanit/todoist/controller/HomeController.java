package com.zayaanit.todoist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Zubayer Ahamed
 * @since Jun 17, 2025
 */
@RestController
@RequestMapping("/home")
public class HomeController {

	@GetMapping
	public ResponseEntity<String> index() {
		return new ResponseEntity<>("Hellow from secured endpoint", HttpStatus.OK);
	}
}

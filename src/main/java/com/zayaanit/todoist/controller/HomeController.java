package com.zayaanit.todoist.controller;

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
	public String index() {
		return "All Ok";
	}
}

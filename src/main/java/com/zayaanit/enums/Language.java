package com.zayaanit.enums;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 5, 2025
 */
public enum Language {

	ENGLISH("English");

	private String prompt;

	Language(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return this.prompt;
	}
}

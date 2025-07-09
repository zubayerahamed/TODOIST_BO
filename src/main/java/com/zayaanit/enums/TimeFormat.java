package com.zayaanit.enums;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 5, 2025
 */
public enum TimeFormat {

	HOUR_12("12 hours: 9.00 PM"),
	HOUR_24("24 hours: 21:00");

	private String prompt;

	TimeFormat(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return this.prompt;
	}
}

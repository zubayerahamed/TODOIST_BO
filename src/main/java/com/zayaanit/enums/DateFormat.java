package com.zayaanit.enums;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 5, 2025
 */
public enum DateFormat {

	DD_MMM_YYYY("31 Dec 2025", "dd MMM yyyy"),
	MMM_DD_YYYY("Dec 31, 2025", "MMM dd, yyyy");

	private String prompt;
	private String pattern;

	DateFormat(String prompt, String pattern) {
		this.prompt = prompt;
		this.pattern = pattern;
	}

	public String getPrompt() {
		return this.prompt;
	}

	public String getPattern() {
		return this.pattern;
	}
}

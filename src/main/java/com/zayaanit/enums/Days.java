package com.zayaanit.enums;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 5, 2025
 */
public enum Days {

	SAT("Saturday"),
	SUN("Sunday"),
	MON("Monday"),
	TUE("Tuesday"),
	WED("Wednesday"),
	THU("Thursday"),
	FRI("Friday");

	private String prompt;

	Days(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return this.prompt;
	}
}

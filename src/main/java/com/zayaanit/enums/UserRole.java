package com.zayaanit.enums;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public enum UserRole {

	USER("ROLE_USER", "User", 0);

	private String code;
	private String roleName;
	private int priority;

	private UserRole(String code, String roleName, int priority) {
		this.code = code;
		this.roleName = roleName;
		this.priority = priority;
	}

	public String getCode() {
		return code;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getPriority() {
		return priority;
	}
}

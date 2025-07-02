package com.zayaanit.todoist.controller.users;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
public class CreateUsersReqDto {

	private String email;
	private String password;
	private String firstName;
	private String lastName;

	public User getBean() {
		return User.builder()
				.email(email)
				.password(password)
				.firstName(firstName)
				.lastName(lastName)
				.build();
	}
}

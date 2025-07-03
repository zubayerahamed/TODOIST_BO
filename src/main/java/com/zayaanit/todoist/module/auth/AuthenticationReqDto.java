package com.zayaanit.todoist.module.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationReqDto {

	private String email;
	private String password;
}

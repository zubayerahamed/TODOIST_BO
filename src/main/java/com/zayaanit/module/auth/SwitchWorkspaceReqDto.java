package com.zayaanit.module.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Aug 12, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwitchWorkspaceReqDto {

	private Long workspaceId;
	private String email;
}

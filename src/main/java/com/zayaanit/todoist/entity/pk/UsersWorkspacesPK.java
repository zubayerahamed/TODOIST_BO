package com.zayaanit.todoist.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersWorkspacesPK implements Serializable {

	private static final long serialVersionUID = 4094500453577564536L;

	private Long userId;
	private Long workspaceId;
}

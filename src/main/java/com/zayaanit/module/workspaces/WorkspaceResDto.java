package com.zayaanit.module.workspaces;

import org.springframework.beans.BeanUtils;

import com.zayaanit.module.users.workspaces.UserWorkspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 5, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResDto {

	private Long id;
	private String name;
	private Boolean isActive;
	private Boolean isSystemDefined;
	private byte[] logo;
	private boolean isPrimary;
	private boolean isAdmin;
	private boolean isCollaborator;

	public WorkspaceResDto(Workspace workspace, UserWorkspace uw) {
		BeanUtils.copyProperties(workspace, this);
		if(uw != null) {
			this.isPrimary = Boolean.TRUE.equals(uw.getIsPrimary());
			this.isAdmin = Boolean.TRUE.equals(uw.getIsAdmin());
			this.isCollaborator = Boolean.TRUE.equals(uw.getIsCollaborator());
		}
	}
}

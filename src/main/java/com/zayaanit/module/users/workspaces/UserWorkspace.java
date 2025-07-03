package com.zayaanit.module.users.workspaces;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UsersWorkspacesPK.class)
@Table(name = "users_workspaces")
@EqualsAndHashCode(callSuper = true)
public class UserWorkspace extends AbstractModel<Long> {

	private static final long serialVersionUID = 8172997495726591542L;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Id
	@Column(name = "workspace_id")
	private Long workspaceId;

	@Column(name = "is_primary", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isPrimary;

	@Column(name = "is_admin", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isAdmin;

	@Column(name = "is_collaborator", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean isCollaborator;

}

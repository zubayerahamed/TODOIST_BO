package com.zayaanit.todoist.dto.res;

import com.zayaanit.todoist.entity.Workspaces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResDto {

	private Long id;
	private String name;
	private Boolean isActive;
	private String logo;

	public WorkspaceResDto(Workspaces zbusiness){
		this.id = zbusiness.getId();
		this.name = zbusiness.getName();
		this.isActive = Boolean.TRUE.equals(zbusiness.getIsActive());
		this.logo = zbusiness.getLogo() == null ? null : zbusiness.getLogo().toString();
	}
}

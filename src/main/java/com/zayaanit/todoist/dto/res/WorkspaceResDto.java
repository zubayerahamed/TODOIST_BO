package com.zayaanit.todoist.dto.res;

import com.zayaanit.todoist.entity.Zbusiness;

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

	public WorkspaceResDto(Zbusiness zbusiness){
		this.id = zbusiness.getZid();
		this.name = zbusiness.getZorg();
		this.isActive = Boolean.TRUE.equals(zbusiness.getZactive());
		this.logo = zbusiness.getXlogo() == null ? null : zbusiness.getXlogo().toString();
	}
}

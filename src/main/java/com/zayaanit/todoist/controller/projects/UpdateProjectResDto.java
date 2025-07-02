package com.zayaanit.todoist.controller.projects;

import org.springframework.beans.BeanUtils;

import com.zayaanit.todoist.enums.LayoutType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectResDto {

	private Long id;
	private Long workspaceId;
	private String name;
	private String color;
	private Boolean isFavourite;
	private Integer seqn;
	private LayoutType layoutType;
	private Boolean isSystemDefined;

	public UpdateProjectResDto(Project project) {
		BeanUtils.copyProperties(project, this);
	}
}

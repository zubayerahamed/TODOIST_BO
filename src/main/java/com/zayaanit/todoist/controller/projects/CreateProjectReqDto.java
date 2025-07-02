package com.zayaanit.todoist.controller.projects;

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
public class CreateProjectReqDto {

	private String name;
	private String color;
	private Boolean isFavourite;
	private LayoutType layoutType;

	public Project getBean() {
		return Project.builder()
				.name(name)
				.color(color == null ? "#000000" : color)
				.isFavourite(isFavourite == null ? Boolean.FALSE : isFavourite)
				.layoutType(layoutType == null ? LayoutType.LIST : layoutType)
				.build();
	}
}

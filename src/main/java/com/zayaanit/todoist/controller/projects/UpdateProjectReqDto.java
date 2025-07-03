package com.zayaanit.todoist.controller.projects;

import com.zayaanit.todoist.controller.ValidEnum;
import com.zayaanit.todoist.enums.LayoutType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UpdateProjectReqDto {

	@NotNull(message = "Project id required")
	private Long id;
	@NotBlank(message = "Project name required.")
	@Size(min = 1, max = 25, message = "Project name must be 1 to 25 characters long")
	private String name;
	private String color;
	@NotNull(message = "Is favourite required, Value should be either true or false")
	private Boolean isFavourite;
	@NotNull(message = "Layout type required, Value should be withing LIST, BOARD, CALENDAR")
	@ValidEnum(enumClass = LayoutType.class, message = "Invalid layout type, Value should be withing LIST, BOARD, CALENDAR")
	private LayoutType layoutType;
}

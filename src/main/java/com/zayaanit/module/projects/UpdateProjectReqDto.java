package com.zayaanit.module.projects;

import com.zayaanit.enums.LayoutType;

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
	private Boolean isFavourite;
	private LayoutType layoutType;
}

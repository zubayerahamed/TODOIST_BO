package com.zayaanit.todoist.controller.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryReqDto {

	@NotNull(message = "Category id required")
	private Long id;
	@NotBlank(message = "Category name required.")
	@Size(min = 1, max = 25, message = "Category name must be 1 to 25 characters long")
	private String name;
	private String color;
	@NotNull(message = "Is for task required, Value should be either true or false")
	private Boolean isForTask;
	@NotNull(message = "Is for event required, Value should be either true or false")
	private Boolean isForEvent;
}

package com.zayaanit.todoist.module.category;

import org.apache.commons.lang3.StringUtils;

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
public class CreateCategoryReqDto {

	private Long referenceId;
	@NotBlank(message = "Category name required.")
	@Size(min = 1, max = 25, message = "Category name must be 1 to 25 characters long")
	private String name;
	private String color;
	@NotNull(message = "Is for task required, Value should be either true or false")
	private Boolean isForTask;
	@NotNull(message = "Is for event required, Value should be either true or false")
	private Boolean isForEvent;

	public Category getBean() {
		return Category.builder()
				.referenceId(referenceId)
				.name(name)
				.color(StringUtils.isBlank(color) ? "#000000" : color)
				.isForTask(isForTask)
				.isForEvent(isForEvent)
				.build();
	}
}

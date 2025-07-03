package com.zayaanit.module.category;

import org.springframework.beans.BeanUtils;

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
public class UpdateCategoryResDto {

	private Long id;
	private Long referenceId;
	private String name;
	private String color;
	private Boolean isForTask;
	private Boolean isForEvent;
	private Integer seqn;

	public UpdateCategoryResDto(Category category) {
		BeanUtils.copyProperties(category, this);
	}
}

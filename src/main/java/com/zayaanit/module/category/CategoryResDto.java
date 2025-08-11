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
public class CategoryResDto {

	private Long id;
	private Long referenceId;
	private String name;
	private String color;
	private Boolean isForTask;
	private Boolean isForEvent;
	private Integer seqn;
	private Boolean isDefaultForTask;
	private Boolean isDefaultForEvent;
	private Boolean isInherited;
	private Long parentId;

	public CategoryResDto(Category category) {
		BeanUtils.copyProperties(category, this);
	}
}

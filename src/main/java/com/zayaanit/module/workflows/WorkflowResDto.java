package com.zayaanit.module.workflows;

import org.springframework.beans.BeanUtils;

import com.zayaanit.enums.ReferenceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Aug 10, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowResDto {

	private Long id;
	private Long referenceId;
	private ReferenceType referenceType;
	private String name;
	private Boolean isSystemDefined;
	private Integer seqn;
	private String color;
	private Boolean isInherited;
	private Long parentId;

	public WorkflowResDto(Workflow workflow) {
		BeanUtils.copyProperties(workflow, this);
	}
}

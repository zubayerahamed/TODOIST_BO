package com.zayaanit.module.sections;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionResDto {

	private Long id;
	private Long projectId;
	private String name;
	private Long workflowId;
	private Integer seqn;

	public SectionResDto(Section section) {
		BeanUtils.copyProperties(section, this);
	}
}

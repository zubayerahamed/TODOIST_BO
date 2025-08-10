package com.zayaanit.module.workflows;

import org.apache.commons.lang3.StringUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CreateWorkflowReqDto {

	private Long referenceId;   // It can be workspace id or can be project id
	@NotBlank(message = "Status name required.")
	@Size(min = 1, max = 25, message = "Status name must be 1 to 25 characters long")
	private String name;
	private Integer seqn;
	private String color;

	public Workflow getBean() {
		return Workflow.builder()
				.referenceId(referenceId)
				.name(name)
				.color(StringUtils.isBlank(color) ? "#000000" : color)
				.isSystemDefined(false)
				.seqn(seqn)
				.build();
	}
}

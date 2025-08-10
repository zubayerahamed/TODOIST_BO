package com.zayaanit.module.workflows;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateWorkflowReqDto {

	@NotNull(message = "Status id required")
	private Long id;
	@NotBlank(message = "Status name required.")
	@Size(min = 1, max = 25, message = "Status name must be 1 to 25 characters long")
	private String name;
	private Integer seqn;
	private String color;
}

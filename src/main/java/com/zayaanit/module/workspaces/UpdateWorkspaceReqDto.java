package com.zayaanit.module.workspaces;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 5, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkspaceReqDto {

	@NotNull(message = "Workspace id required")
	private Long id;
	@NotBlank(message = "Workspace name required.")
	@Size(min = 1, max = 25, message = "Workspace name must be 1 to 25 characters long")
	private String name;
}

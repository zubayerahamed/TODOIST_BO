package com.zayaanit.todoist.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResDto {

	private Long zid;
	private String zorg;
	private Boolean zactive;
	private byte[] xlogo;
}

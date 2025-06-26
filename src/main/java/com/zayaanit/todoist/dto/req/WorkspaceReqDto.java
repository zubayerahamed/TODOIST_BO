package com.zayaanit.todoist.dto.req;

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
public class WorkspaceReqDto {

	private Long zid;
	private String zorg;
	private Boolean zactive;
	private byte[] xlogo;
}

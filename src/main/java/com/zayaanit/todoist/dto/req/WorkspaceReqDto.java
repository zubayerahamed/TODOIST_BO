package com.zayaanit.todoist.dto.req;

import com.zayaanit.todoist.entity.Zbusiness;

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

	private Long id;
	private String name;
	private Boolean isActive;
	private String logo;

	public Zbusiness getBean() {
		return Zbusiness.builder()
			.zid(id)
			.zorg(name)
			.zactive(isActive)
			.xlogo(logo.getBytes())
			.build();
	}
}

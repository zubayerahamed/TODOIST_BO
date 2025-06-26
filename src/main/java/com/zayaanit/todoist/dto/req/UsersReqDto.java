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
public class UsersReqDto {

	private Long zuser;
	private String zemail;
	private String xpassword;
	private String xfname;
	private String xlname;
	private Boolean zactive;
}

package com.zayaanit.module.users.preferences;

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
public class UpdateUserPreferenceReqDto {

	private Long userId;
	private String language;
	private String homeView;
	private String timeZone;
	private String timeFormat;
	private String dateFormat;
	private String weekStart;
	private String nextWeek;
	private String weekend;
}

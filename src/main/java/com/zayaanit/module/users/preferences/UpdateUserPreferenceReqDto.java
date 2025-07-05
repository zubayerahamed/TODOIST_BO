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
	private String home_view;
	private String time_zone;
	private String time_format;
	private String date_format;
	private String week_start;
	private String next_week;
	private String weekend;
}

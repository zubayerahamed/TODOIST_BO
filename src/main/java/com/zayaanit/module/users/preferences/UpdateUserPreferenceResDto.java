package com.zayaanit.module.users.preferences;

import org.springframework.beans.BeanUtils;

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
public class UpdateUserPreferenceResDto {

	private Long userId;
	private String language;
	private String home_view;
	private String time_zone;
	private String time_format;
	private String date_format;
	private String week_start;
	private String next_week;
	private String weekend;
	private Boolean enabled_browser_noti;
	private Boolean enabled_push_noti;
	private Boolean enabled_email_noti;
	private Boolean enabled_sms_noti;

	public UpdateUserPreferenceResDto(UserPreference obj) {
		BeanUtils.copyProperties(obj, this);
	}
}

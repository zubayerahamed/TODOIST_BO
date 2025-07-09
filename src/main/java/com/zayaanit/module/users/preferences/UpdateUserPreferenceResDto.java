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
	private String homeView;
	private String timeZone;
	private String timeFormat;
	private String dateFormat;
	private String weekStart;
	private String nextWeek;
	private String weekend;
	private Boolean enabledBrowserNoti;
	private Boolean enabledPushNoti;
	private Boolean enabledEmailNoti;
	private Boolean enabledSmsNoti;

	public UpdateUserPreferenceResDto(UserPreference obj) {
		BeanUtils.copyProperties(obj, this);
	}
}

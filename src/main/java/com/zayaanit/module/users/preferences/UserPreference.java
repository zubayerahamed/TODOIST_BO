package com.zayaanit.module.users.preferences;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 5, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_preferences")
@EqualsAndHashCode(callSuper = true)
public class UserPreference extends AbstractModel<Long> {

	private static final long serialVersionUID = -3481371981928931227L;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "language", length = 25)
	private String language;

	@Column(name = "home_view", length = 25)
	private String homeView;

	@Column(name = "time_zone", length = 25)
	private String timeZone;

	@Column(name = "time_format", length = 25)
	private String timeFormat;

	@Column(name = "date_format", length = 25)
	private String dateFormat;

	@Column(name = "week_start", length = 25)
	private String weekStart;

	@Column(name = "next_week", length = 25)
	private String nextWeek;

	@Column(name = "weekend", length = 25)
	private String weekend;

	@Column(name = "enabled_browser_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabledBrowserNoti;

	@Column(name = "enabled_push_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabledPushNoti;

	@Column(name = "enabled_email_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabledEmailNoti;

	@Column(name = "enabled_sms_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabledSmsNoti;
}

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
	private String home_view;

	@Column(name = "time_zone", length = 25)
	private String time_zone;

	@Column(name = "time_format", length = 25)
	private String time_format;

	@Column(name = "date_format", length = 25)
	private String date_format;

	@Column(name = "week_start", length = 25)
	private String week_start;

	@Column(name = "next_week", length = 25)
	private String next_week;

	@Column(name = "weekend", length = 25)
	private String weekend;

	@Column(name = "enabled_browser_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabled_browser_noti;

	@Column(name = "enabled_push_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabled_push_noti;

	@Column(name = "enabled_email_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabled_email_noti;

	@Column(name = "enabled_sms_noti", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean enabled_sms_noti;
}

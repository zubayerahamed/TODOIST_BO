package com.zayaanit.module.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 15, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

	private NotificationType status;
	private String title;
	private String message;
}

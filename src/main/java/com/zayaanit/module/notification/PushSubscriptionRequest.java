package com.zayaanit.module.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 16, 2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushSubscriptionRequest {
	private Long userId;
	private Subscription subscription;
}

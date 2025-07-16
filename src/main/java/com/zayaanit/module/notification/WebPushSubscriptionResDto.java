package com.zayaanit.module.notification;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 16, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebPushSubscriptionResDto {

	private Long id;
	private Long userId;
	private String endpoint;
	private String publicKey;
	private String auth;

	WebPushSubscriptionResDto(WebPushSubscription obj){
		BeanUtils.copyProperties(obj, this);
	}
}

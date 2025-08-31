package com.zayaanit.module.notification;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 16, 2025
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "web_push_subscriptions")
@EqualsAndHashCode(callSuper = true)
public class WebPushSubscription extends AbstractModel<Long> {

	private static final long serialVersionUID = 7736453503808160198L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "endpoint", length = 2048)
	private String endpoint;

	@Column(name = "public_key")
	private String publicKey;

	@Column(name = "auth")
	private String auth;

	public Subscription getSubscription() {
		Subscription sub = new Subscription();
		sub.endpoint = this.endpoint;
		sub.keys = new Subscription.Keys(this.publicKey,this.auth);
		return sub;
	}
}

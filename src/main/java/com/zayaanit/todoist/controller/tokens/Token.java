package com.zayaanit.todoist.controller.tokens;

import java.io.Serializable;

import com.zayaanit.todoist.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token implements Serializable {

	private static final long serialVersionUID = 5429097916566992030L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(unique = true, nullable = false)
	public String token;

	@Enumerated(EnumType.STRING)
	public TokenType xtype;

	@Column(name = "is_revoked")
	public boolean revoked;

	@Column(name = "is_expired")
	public boolean expired;

	@Column(name = "user_id")
	public Long userId;
}

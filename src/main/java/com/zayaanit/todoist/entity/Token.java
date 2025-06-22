package com.zayaanit.todoist.entity;

import com.zayaanit.todoist.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 22, 2025
 */
public class Token {

	@Id
	@GeneratedValue
	public Integer id;

	@Column(unique = true)
	public String token;

	@Enumerated(EnumType.STRING)
	public TokenType tokenType = TokenType.BEARER;

	public boolean revoked;

	public boolean expired;

	@Column(name = "zuser")
	public Integer zuser;
}

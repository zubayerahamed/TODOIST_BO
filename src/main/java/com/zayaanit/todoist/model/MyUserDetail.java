package com.zayaanit.todoist.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zayaanit.todoist.entity.Users;
import com.zayaanit.todoist.entity.Workspaces;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public class MyUserDetail implements UserDetails {

	private static final long serialVersionUID = 9078767219064029540L;

	private Long zuser;
	private String zemail;
	private String xpassword;
	private Workspaces zbusiness;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetail(Users xuser, Workspaces zbusiness){
		this.zuser = xuser.getId();
		this.zemail = xuser.getEmail();
		this.xpassword = xuser.getPassword();
		this.zbusiness = zbusiness;
		this.roles = "ROLE_USER";
		this.authorities = Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.xpassword;
	}

	@Override
	public String getUsername() {
		return this.zuser.toString();
	}

	public String getUserEmail() {
		return this.zemail;
	}

	public Workspaces getZbusiness() {
		return this.zbusiness;
	}

}

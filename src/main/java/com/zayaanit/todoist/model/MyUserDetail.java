package com.zayaanit.todoist.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.entity.Zbusiness;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public class MyUserDetail implements UserDetails {

	private static final long serialVersionUID = 9078767219064029540L;

	private Integer zuser;
	private String zemail;
	private String xpassword;
	private Zbusiness zbusiness;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetail(Xusers xuser, Zbusiness zbusiness){
		this.zuser = xuser.getZuser();
		this.zemail = xuser.getZemail();
		this.xpassword = xuser.getXpassword();
		this.zbusiness = zbusiness;
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

	public Zbusiness getZbusiness() {
		return this.zbusiness;
	}

}

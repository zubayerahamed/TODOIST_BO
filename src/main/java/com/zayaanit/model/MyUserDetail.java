package com.zayaanit.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zayaanit.module.users.User;
import com.zayaanit.module.users.workspaces.UserWorkspace;
import com.zayaanit.module.workspaces.Workspace;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public class MyUserDetail implements UserDetails {

	private static final long serialVersionUID = 9078767219064029540L;

	private Long id;
	private String email;
	private String password;
	private Workspace workspace;
	private String roles;
	private boolean primaryWorkspace;
	private List<GrantedAuthority> authorities;

	public MyUserDetail(User user, Workspace workspace, UserWorkspace userWorkspace){
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.workspace = workspace;
		this.primaryWorkspace = Boolean.TRUE.equals(userWorkspace.getIsPrimary());
		this.roles = "ROLE_USER";
		if(Boolean.TRUE.equals(userWorkspace.getIsAdmin())) {
			roles += ",ROLE_ADMIN";
		} else if(Boolean.TRUE.equals(userWorkspace.getIsCollaborator())) {
			roles += ",ROLE_COLLABORATOR";
		}
		this.authorities = Arrays.stream(roles.split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.id.toString();
	}

	public String getEmail() {
		return this.email;
	}

	public Workspace getWorkspace() {
		return this.workspace;
	}

	public boolean isPrimaryWorkspace() {
		return this.primaryWorkspace;
	}
}

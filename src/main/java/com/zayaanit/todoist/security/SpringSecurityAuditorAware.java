package com.zayaanit.todoist.security;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zayaanit.todoist.model.MyUserDetail;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public class SpringSecurityAuditorAware implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		Integer id = Integer.valueOf(0);
		MyUserDetail user = getLoggedInUserDetails();
		if(user != null && StringUtils.isNotBlank(user.getUsername())) id = Integer.parseInt(user.getUsername());
		return Optional.ofNullable(id);
	}

	public MyUserDetail getLoggedInUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(!(principal instanceof MyUserDetail)) return null;
		return (MyUserDetail) principal;
	}
}

package com.zayaanit.config;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zayaanit.model.MyUserDetail;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public class DataAuditorAware implements AuditorAware<Long> {

	@Override
	public @NonNull Optional<Long> getCurrentAuditor() {
		Long id = Long.valueOf(0);
		MyUserDetail user = getLoggedInUserDetails();
		if(user != null && StringUtils.isNotBlank(user.getUsername())) id = Long.parseLong(user.getUsername());
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

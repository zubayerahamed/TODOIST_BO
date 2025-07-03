package com.zayaanit.module;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zayaanit.exception.CustomException;
import com.zayaanit.model.MyUserDetail;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
public abstract class BaseService {

	protected MyUserDetail loggedinUser() throws CustomException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new CustomException("User not authenticated", HttpStatus.FORBIDDEN);
		}

		Object principal = auth.getPrincipal();
		if (principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			return mud;
		}

		throw new CustomException("User not authenticated", HttpStatus.FORBIDDEN);
	}
}

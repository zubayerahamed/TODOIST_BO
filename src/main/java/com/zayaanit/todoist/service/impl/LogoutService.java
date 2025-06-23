package com.zayaanit.todoist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Xtokens;
import com.zayaanit.todoist.repo.XtokensRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Zubayer Ahamed
 * @since Jun 23, 2025
 */
@Service
public class LogoutService implements LogoutHandler {

	@Autowired private XtokensRepo xtokensRepo;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwtToken;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		jwtToken = authHeader.substring(7);

		Xtokens storedToken = xtokensRepo.findByXtoken(jwtToken).orElse(null);
		if(storedToken != null) {
			storedToken.setXexpired(true);
			storedToken.setXrevoked(true);
			xtokensRepo.save(storedToken);
		}

	}

}

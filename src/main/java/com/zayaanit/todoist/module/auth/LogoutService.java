package com.zayaanit.todoist.module.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.module.tokens.Token;
import com.zayaanit.todoist.module.tokens.TokenRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Zubayer Ahamed
 * @since Jun 23, 2025
 */
@Service
public class LogoutService implements LogoutHandler {

	@Autowired private TokenRepo tokensRepo;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwtToken;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		jwtToken = authHeader.substring(7);

		Token storedToken = tokensRepo.findByToken(jwtToken).orElse(null);
		if(storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokensRepo.save(storedToken);
		}

	}

}

package com.zayaanit.todoist.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.repo.TokensRepo;
import com.zayaanit.todoist.service.XusersService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final XusersService xusersService;
	private final TokensRepo xtokensRepo;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request, 
		@NonNull HttpServletResponse response, 
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwtToken;
		final String userId;

		if(StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwtToken = authHeader.substring(7);
		userId = jwtService.extractUsername(jwtToken);
		if(StringUtils.isNotBlank(userId) && SecurityContextHolder.getContext().getAuthentication() == null) {  // User id not null && user is not authenticated yet
			MyUserDetail userDetails = (MyUserDetail) xusersService.loadUserByUsername(userId);

			var isTokenValid = xtokensRepo.findByToken(jwtToken).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);

			if(jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}

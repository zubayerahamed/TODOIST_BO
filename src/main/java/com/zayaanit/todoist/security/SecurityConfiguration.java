package com.zayaanit.todoist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zayaanit.todoist.service.XusersService;
import com.zayaanit.todoist.service.impl.LogoutService;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 22, 2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired private XusersService xusersService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired private LogoutService logoutHandler;

	private static final String[] WHITE_LIST_URL = new String[] { "/auth/**", };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
				auth -> auth.requestMatchers(WHITE_LIST_URL).permitAll()
							.anyRequest().authenticated()
			)
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(
				l -> l.logoutUrl("/api/v1/auth/logout")
						.addLogoutHandler(logoutHandler)
						.logoutSuccessHandler(
							(request, response, authentication) -> SecurityContextHolder.clearContext()
						)
			);

		return http.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(xusersService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}

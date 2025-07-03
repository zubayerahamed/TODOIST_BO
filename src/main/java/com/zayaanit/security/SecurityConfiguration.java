package com.zayaanit.security;

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

import com.zayaanit.exception.CustomAccessDeniedHandler;
import com.zayaanit.exception.CustomAuthenticationEntryPoint;
import com.zayaanit.module.auth.LogoutService;
import com.zayaanit.module.users.UserService;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 22, 2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired private UserService usersService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired private LogoutService logoutHandler;
	@Autowired private CustomAccessDeniedHandler accessDeniedHandler;
	@Autowired private CustomAuthenticationEntryPoint authenticationEntryPoint;

	private static final String[] WHITE_LIST_URL = new String[] { 
		"/api/v1/auth/**",
	};

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
				auth -> auth.requestMatchers(WHITE_LIST_URL).permitAll()
							.anyRequest().authenticated()
			)
			.exceptionHandling(
				ex -> ex.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint)
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
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(usersService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}

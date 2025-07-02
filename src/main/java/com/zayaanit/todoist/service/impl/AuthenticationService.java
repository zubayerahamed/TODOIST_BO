package com.zayaanit.todoist.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zayaanit.todoist.entity.Tokens;
import com.zayaanit.todoist.entity.Users;
import com.zayaanit.todoist.entity.UsersWorkspaces;
import com.zayaanit.todoist.entity.Workspaces;
import com.zayaanit.todoist.enums.TokenType;
import com.zayaanit.todoist.exception.CustomException;
import com.zayaanit.todoist.model.AuthenticationRequest;
import com.zayaanit.todoist.model.AuthenticationResponse;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.model.RegisterRequest;
import com.zayaanit.todoist.repo.TokensRepo;
import com.zayaanit.todoist.repo.UsersRepo;
import com.zayaanit.todoist.repo.UsersWorkspacesRepo;
import com.zayaanit.todoist.repo.WorkspacesRepo;
import com.zayaanit.todoist.security.JwtService;
import com.zayaanit.todoist.service.XusersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * 
 * @since Jun 22, 2025
 */
@Service
public class AuthenticationService {

	@Autowired private WorkspacesRepo zbusinessRepo;
	@Autowired private UsersRepo xusersRepo;
	@Autowired private UsersWorkspacesRepo xusersZbusinessRepo;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private TokensRepo tokensRepo;
	@Autowired private XusersService xusersService;

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) {
		// 1. Check if user already exists
		if (xusersRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new CustomException("Email is already registered.", HttpStatus.BAD_REQUEST);
		}

		// 2. Create user
		Users xusers = Users.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.isActive(Boolean.TRUE).build();

		xusers = xusersRepo.save(xusers);

		// 3. Create workspace (business)
		Workspaces business = Workspaces.builder()
				.name(xusers.getFirstName() + "'s Workspace")
				.isActive(Boolean.TRUE)
				.build();

		business = zbusinessRepo.save(business);

		// 4. Create user-business relationship
		UsersWorkspaces xz = UsersWorkspaces.builder()
				.userId(xusers.getId())
				.workspaceId(business.getId())
				.isAdmin(Boolean.TRUE)
				.isCollaborator(Boolean.FALSE).build();

		xz = xusersZbusinessRepo.save(xz);

		// 5. Generate JWT token and Refresh token
		var jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));
		var refreshToken = jwtService.generateRefreshToken(new MyUserDetail(xusers, business));

		// 6. Save User Token
		saveUserToken(xusers.getId(), jwtToken);

		// 7. Return token
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// 1. Find user by email
		Users xusers = xusersRepo.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email is not registered."));

		// 2. Check if user is active
		if (Boolean.FALSE.equals(xusers.getIsActive())) {
			throw new RuntimeException("User account is inactive.");
		}

		// 3. Verify password
		if (!passwordEncoder.matches(request.getPassword(), xusers.getPassword())) {
			throw new RuntimeException("Invalid credentials.");
		}

		// 4. Get primary business assigned to this user
		UsersWorkspaces primaryLink = xusersZbusinessRepo.findByUserIdAndIsPrimary(xusers.getId(), Boolean.TRUE).orElseThrow(() -> new RuntimeException("No primary workspace assigned to this user."));

		// 5. Load the business
		Workspaces business = zbusinessRepo.findById(primaryLink.getWorkspaceId()).orElseThrow(() -> new RuntimeException("Primary workspace not found."));

		// 6. Check if business is active
		if (Boolean.FALSE.equals(business.getIsActive())) {
			throw new RuntimeException("Primary workspace is inactive.");
		}

		// 7. Generate token
		var jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));
		var refreshToken = jwtService.generateRefreshToken(new MyUserDetail(xusers, business));

		// 8. Revoke tokens and save new token
		revokeAllUserTokens(xusers.getId());
		saveUserToken(xusers.getId(), jwtToken);

		// 9. Return token
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	@Transactional
	private void revokeAllUserTokens(Long zuser) {
		List<Tokens> validTokens = tokensRepo.findAllByUserIdAndRevokedAndExpired(zuser, false, false);
		if (validTokens.isEmpty())
			return;

		validTokens.forEach(t -> {
			t.setRevoked(true);
			t.setExpired(true);
		});

		tokensRepo.saveAll(validTokens);
	}

	@Transactional
	private void saveUserToken(Long zuser, String jwtToken) {
		Tokens xtoken = Tokens.builder()
				.userId(zuser)
				.token(jwtToken)
				.revoked(false)
				.expired(false)
				.xtype(TokenType.BEARER)
				.build();

		tokensRepo.save(xtoken);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userId;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userId = jwtService.extractUsername(refreshToken);
		if (StringUtils.isNotBlank(userId)) {
			MyUserDetail userDetails = (MyUserDetail) xusersService.loadUserByUsername(userId);

			if (jwtService.isTokenValid(refreshToken, userDetails)) {
				var accessToken = jwtService.generateToken(userDetails);
				revokeAllUserTokens(Long.valueOf(userDetails.getUsername()));
				saveUserToken(Long.valueOf(userDetails.getUsername()), accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

}

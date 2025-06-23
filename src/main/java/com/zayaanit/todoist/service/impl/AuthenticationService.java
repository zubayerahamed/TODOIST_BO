package com.zayaanit.todoist.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zayaanit.todoist.entity.Xtokens;
import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.entity.XusersZbusiness;
import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.enums.TokenType;
import com.zayaanit.todoist.model.AuthenticationRequest;
import com.zayaanit.todoist.model.AuthenticationResponse;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.model.RegisterRequest;
import com.zayaanit.todoist.repo.XtokensRepo;
import com.zayaanit.todoist.repo.XusersRepo;
import com.zayaanit.todoist.repo.XusersZbusinessRepo;
import com.zayaanit.todoist.repo.ZbusinessRepo;
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

	@Autowired private ZbusinessRepo zbusinessRepo;
	@Autowired private XusersRepo xusersRepo;
	@Autowired private XusersZbusinessRepo xusersZbusinessRepo;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private XtokensRepo xtokensRepo;
	@Autowired private XusersService xusersService;

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) {
		// 1. Check if user already exists
		if (xusersRepo.findByZemail(request.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already registered.");
		}

		// 2. Create user
		Xusers xusers = Xusers.builder()
				.xfname(request.getFirstName())
				.xlname(request.getLastName())
				.zemail(request.getEmail())
				.xpassword(passwordEncoder.encode(request.getPassword()))
				.zactive(Boolean.TRUE).build();

		xusers = xusersRepo.save(xusers);

		// 3. Create workspace (business)
		Zbusiness business = Zbusiness.builder()
				.zorg(xusers.getXfname() + "'s Workspace")
				.zactive(Boolean.TRUE)
				.build();

		business = zbusinessRepo.save(business);

		// 4. Create user-business relationship
		XusersZbusiness xz = XusersZbusiness.builder()
				.zuser(xusers.getZuser())
				.zid(business.getZid())
				.zprimary(Boolean.TRUE)
				.zadmin(Boolean.TRUE)
				.zcollaborator(Boolean.FALSE).build();

		xz = xusersZbusinessRepo.save(xz);

		// 5. Generate JWT token and Refresh token
		var jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));
		var refreshToken = jwtService.generateRefreshToken(new MyUserDetail(xusers, business));

		// 6. Save User Token
		saveUserToken(xusers.getZuser(), jwtToken);

		// 7. Return token
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// 1. Find user by email
		Xusers xusers = xusersRepo.findByZemail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email is not registered."));

		// 2. Check if user is active
		if (Boolean.FALSE.equals(xusers.getZactive())) {
			throw new RuntimeException("User account is inactive.");
		}

		// 3. Verify password
		if (!passwordEncoder.matches(request.getPassword(), xusers.getXpassword())) {
			throw new RuntimeException("Invalid credentials.");
		}

		// 4. Get primary business assigned to this user
		XusersZbusiness primaryLink = xusersZbusinessRepo.findByZuserAndZprimary(xusers.getZuser(), Boolean.TRUE).orElseThrow(() -> new RuntimeException("No primary workspace assigned to this user."));

		// 5. Load the business
		Zbusiness business = zbusinessRepo.findById(primaryLink.getZid()).orElseThrow(() -> new RuntimeException("Primary workspace not found."));

		// 6. Check if business is active
		if (Boolean.FALSE.equals(business.getZactive())) {
			throw new RuntimeException("Primary workspace is inactive.");
		}

		// 7. Generate token
		var jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));
		var refreshToken = jwtService.generateRefreshToken(new MyUserDetail(xusers, business));

		// 8. Revoke tokens and save new token
		revokeAllUserTokens(xusers.getZuser());
		saveUserToken(xusers.getZuser(), jwtToken);

		// 9. Return token
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	@Transactional
	private void revokeAllUserTokens(Long zuser) {
		List<Xtokens> validTokens = xtokensRepo.findAllByZuserAndXrevokedAndXexpired(zuser, false, false);
		if (validTokens.isEmpty())
			return;

		validTokens.forEach(t -> {
			t.setXrevoked(true);
			t.setXexpired(true);
		});

		xtokensRepo.saveAll(validTokens);
	}

	@Transactional
	private void saveUserToken(Long zuser, String jwtToken) {
		Xtokens xtoken = Xtokens.builder()
				.zuser(zuser)
				.xtoken(jwtToken)
				.xrevoked(false)
				.xexpired(false)
				.xtype(TokenType.BEARER)
				.build();

		xtokensRepo.save(xtoken);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (StringUtils.isNotBlank(userEmail)) {
			MyUserDetail userDetails = (MyUserDetail) xusersService.loadUserByUsername(userEmail);

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

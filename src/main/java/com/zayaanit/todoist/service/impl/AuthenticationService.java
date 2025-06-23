package com.zayaanit.todoist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.entity.XusersZbusiness;
import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.model.AuthenticationRequest;
import com.zayaanit.todoist.model.AuthenticationResponse;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.model.RegisterRequest;
import com.zayaanit.todoist.repo.XusersRepo;
import com.zayaanit.todoist.repo.XusersZbusinessRepo;
import com.zayaanit.todoist.repo.ZbusinessRepo;
import com.zayaanit.todoist.security.JwtService;

import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class AuthenticationService {

	@Autowired private ZbusinessRepo zbusinessRepo;
	@Autowired private XusersRepo xusersRepo;
	@Autowired private XusersZbusinessRepo xusersZbusinessRepo;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;

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
				.zactive(Boolean.TRUE)
				.build();

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
				.zcollaborator(Boolean.FALSE)
				.build();

		xz = xusersZbusinessRepo.save(xz);

		// 5. Generate JWT token
		var jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));

		// 6. Return token
		return AuthenticationResponse.builder().token(jwtToken).build();
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
		String jwtToken = jwtService.generateToken(new MyUserDetail(xusers, business));

		// 8. Return token
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

}

package com.zayaanit.todoist.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.model.AuthenticationRequest;
import com.zayaanit.todoist.model.AuthenticationResponse;
import com.zayaanit.todoist.model.RegisterRequest;
import com.zayaanit.todoist.security.JwtService;
import com.zayaanit.todoist.service.XusersService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class AuthenticationService {

	@Autowired private XusersService xusersService;
	@Autowired private JwtService jwtService;

	public AuthenticationResponse register(RegisterRequest request) {

		Xusers xusers = new Xusers();
		xusers.setZemail(request.getEmail());
		xusers.setXpassword(request.getPassword());
		xusers.setZactive(Boolean.TRUE);
		xusers.setZuser(100);
		xusers = xusersService.createUser(xusers);
		
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}

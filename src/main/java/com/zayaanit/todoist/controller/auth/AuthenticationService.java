package com.zayaanit.todoist.controller.auth;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zayaanit.todoist.controller.projects.Project;
import com.zayaanit.todoist.controller.projects.ProjectRepo;
import com.zayaanit.todoist.controller.tokens.Token;
import com.zayaanit.todoist.controller.tokens.TokenRepo;
import com.zayaanit.todoist.controller.users.User;
import com.zayaanit.todoist.controller.users.UserRepo;
import com.zayaanit.todoist.controller.users.UserService;
import com.zayaanit.todoist.controller.users.workspaces.UserWorkspace;
import com.zayaanit.todoist.controller.users.workspaces.UserWorkspaceRepo;
import com.zayaanit.todoist.controller.workflows.Workflow;
import com.zayaanit.todoist.controller.workflows.WorkflowRepo;
import com.zayaanit.todoist.controller.workspaces.Workspace;
import com.zayaanit.todoist.controller.workspaces.WorkspaceRepo;
import com.zayaanit.todoist.enums.LayoutType;
import com.zayaanit.todoist.enums.TokenType;
import com.zayaanit.todoist.exception.CustomException;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.security.JwtService;

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

	@Autowired private WorkspaceRepo workspaceRepo;
	@Autowired private UserRepo userRepo;
	@Autowired private UserWorkspaceRepo userWorkspacesRepo;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private TokenRepo tokensRepo;
	@Autowired private UserService userService;
	@Autowired private ProjectRepo projectRepo;
	@Autowired private WorkflowRepo workflowRepo;

	@Transactional
	public AuthenticationResDto register(RegisterRequestDto request) {
		// 1. Check if user already exists
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			throw new CustomException("Email is already registered.", HttpStatus.BAD_REQUEST);
		}

		// 2. Create user
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.isActive(Boolean.TRUE).build();

		user = userRepo.save(user);

		// 3. Create workspace (business)
		Workspace workspace = Workspace.builder()
				.name(user.getFirstName() + "'s Workspace")
				.isSystemDefined(Boolean.TRUE)
				.isActive(Boolean.TRUE)
				.build();

		workspace = workspaceRepo.save(workspace);

		// 4. Create user-business relationship
		UserWorkspace userWorkspace = UserWorkspace.builder()
				.userId(user.getId())
				.workspaceId(workspace.getId())
				.isPrimary(Boolean.TRUE)
				.isAdmin(Boolean.TRUE)
				.isCollaborator(Boolean.FALSE).build();

		userWorkspace = userWorkspacesRepo.save(userWorkspace);

		// 5. Create default project (Index)
		Project project = Project.builder()
				.workspaceId(workspace.getId())
				.name("Inbox")
				.color("#000000")
				.seqn(-999)
				.layoutType(LayoutType.LIST)
				.isSystemDefined(Boolean.TRUE)
				.isFavourite(Boolean.FALSE)
				.build();

		project  = projectRepo.save(project);

		// 6. Create a default status (Completed)
		Workflow workflow = Workflow.builder()
				.referenceId(workspace.getId())
				.name("Completed")
				.isSystemDefined(Boolean.TRUE)
				.seqn(999)
				.color("#000000")
				.build();

		workflow = workflowRepo.save(workflow);

		// 7. Generate JWT token and Refresh token
		var jwtToken = jwtService.generateToken(new MyUserDetail(user, workspace));
		var refreshToken = jwtService.generateRefreshToken(new MyUserDetail(user, workspace));

		// 8. Save User Token
		saveUserToken(user.getId(), jwtToken);

		// 9. Return token
		return AuthenticationResDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	@Transactional
	public AuthenticationResDto authenticate(AuthenticationReqDto request) {
		// 1. Find user by email
		User xusers = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email is not registered."));

		// 2. Check if user is active
		if (Boolean.FALSE.equals(xusers.getIsActive())) {
			throw new RuntimeException("User account is inactive.");
		}

		// 3. Verify password
		if (!passwordEncoder.matches(request.getPassword(), xusers.getPassword())) {
			throw new RuntimeException("Invalid credentials.");
		}

		// 4. Get primary business assigned to this user
		UserWorkspace primaryLink = userWorkspacesRepo.findByUserIdAndIsPrimary(xusers.getId(), Boolean.TRUE).orElseThrow(() -> new RuntimeException("No primary workspace assigned to this user."));

		// 5. Load the business
		Workspace business = workspaceRepo.findById(primaryLink.getWorkspaceId()).orElseThrow(() -> new RuntimeException("Primary workspace not found."));

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
		return AuthenticationResDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	@Transactional
	private void revokeAllUserTokens(Long zuser) {
		List<Token> validTokens = tokensRepo.findAllByUserIdAndRevokedAndExpired(zuser, false, false);
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
		Token xtoken = Token.builder()
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
			MyUserDetail userDetails = (MyUserDetail) userService.loadUserByUsername(userId);

			if (jwtService.isTokenValid(refreshToken, userDetails)) {
				var accessToken = jwtService.generateToken(userDetails);
				revokeAllUserTokens(Long.valueOf(userDetails.getUsername()));
				saveUserToken(Long.valueOf(userDetails.getUsername()), accessToken);
				var authResponse = AuthenticationResDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

}

package com.zayaanit.todoist;

import com.zayaanit.model.MyUserDetail;
import com.zayaanit.module.tokens.Token;
import com.zayaanit.module.tokens.TokenRepo;
import com.zayaanit.module.users.User;
import com.zayaanit.module.users.UserService;
import com.zayaanit.module.users.workspaces.UserWorkspace;
import com.zayaanit.module.workspaces.Workspace;
import com.zayaanit.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public abstract class BaseAuthenticatedControllerTest extends BaseControllerTest {

    protected static final String MOCK_JWT = "mock.jwt.token";
    protected static final String AUTH_HEADER = "Bearer " + MOCK_JWT;

    @MockBean protected JwtService jwtService;
    @MockBean protected TokenRepo tokenRepo;
    @MockBean protected UserService userService;

    protected MyUserDetail authenticatedUser;

    @BeforeEach
    void setUpAuthentication() {
        // Create a mock User entity
        User user = new User();
        user.setId(1L);
        user.setEmail("testuser@example.com");
        user.setPassword("test-password");

        // Create a mock Workspace
        Workspace workspace = new Workspace();
        workspace.setId(100L);
        workspace.setName("Test Workspace");

        // Simulate user access in workspace
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setIsPrimary(true);
        userWorkspace.setIsAdmin(true); // Will add ROLE_ADMIN in MyUserDetail

        // Create authenticated user
        authenticatedUser = new MyUserDetail(user, workspace, userWorkspace);

        // Set up mocks
        when(jwtService.extractUsername(MOCK_JWT)).thenReturn("1");
        when(jwtService.isTokenValid(eq(MOCK_JWT), any())).thenReturn(true);
        when(tokenRepo.findByToken(MOCK_JWT)).thenReturn(Optional.of(new Token())); // not revoked/expired
        when(userService.loadUserByUsername("1")).thenReturn(authenticatedUser);
    }

    protected HttpHeaders authorizedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

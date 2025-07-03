package com.zayaanit.module.users;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.model.MyUserDetail;
import com.zayaanit.module.users.workspaces.UserWorkspace;
import com.zayaanit.module.users.workspaces.UserWorkspaceRepo;
import com.zayaanit.module.workspaces.Workspace;
import com.zayaanit.module.workspaces.WorkspaceRepo;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Service
public class UserService implements UserDetailsService  {

	@Autowired private UserRepo usersRepo;
	@Autowired private UserWorkspaceRepo usersWorkspacesRepo;
	@Autowired private WorkspaceRepo workspacesRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("Email required");
		}

		Optional<User> userOp = usersRepo.findById(Long.valueOf(username));
		if(!userOp.isPresent()) throw new UsernameNotFoundException("User not exist.");

		User user = userOp.get();
		if(Boolean.FALSE.equals(user.getIsActive())) {
			throw new UsernameNotFoundException("User inactive.");
		}

		Optional<UserWorkspace> userWorkspaceOp = usersWorkspacesRepo.findByUserIdAndIsPrimary(user.getId(), Boolean.TRUE);
		if(!userWorkspaceOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Optional<Workspace> workspaceOp = workspacesRepo.findById(userWorkspaceOp.get().getWorkspaceId());
		if(!workspaceOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Workspace workspace = workspaceOp.get();
		if(Boolean.FALSE.equals(workspace.getIsActive())) {
			throw new UsernameNotFoundException("Workspace is disabled");
		}

		return new MyUserDetail(user, workspace, userWorkspaceOp.get());
	}

}

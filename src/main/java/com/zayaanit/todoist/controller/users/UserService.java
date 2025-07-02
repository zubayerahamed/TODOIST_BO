package com.zayaanit.todoist.controller.users;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.controller.users.workspaces.UserWorkspace;
import com.zayaanit.todoist.controller.users.workspaces.UserWorkspaceRepo;
import com.zayaanit.todoist.controller.workspaces.Workspace;
import com.zayaanit.todoist.controller.workspaces.WorkspaceRepo;
import com.zayaanit.todoist.model.MyUserDetail;

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

		Optional<User> xusersOp = usersRepo.findById(Long.valueOf(username));
		if(!xusersOp.isPresent()) throw new UsernameNotFoundException("User not exist.");

		User xuser = xusersOp.get();
		if(Boolean.FALSE.equals(xuser.getIsActive())) {
			throw new UsernameNotFoundException("User inactive.");
		}

		Optional<UserWorkspace> xusersZbusinessOp = usersWorkspacesRepo.findByUserIdAndIsPrimary(xuser.getId(), Boolean.TRUE);
		if(!xusersZbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Optional<Workspace> zbusinessOp = workspacesRepo.findById(xusersZbusinessOp.get().getWorkspaceId());
		if(!zbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Workspace zbusiness = zbusinessOp.get();
		if(Boolean.FALSE.equals(zbusiness.getIsActive())) {
			throw new UsernameNotFoundException("Workspace is disabled");
		}

		return new MyUserDetail(xuser, zbusiness);
	}

}

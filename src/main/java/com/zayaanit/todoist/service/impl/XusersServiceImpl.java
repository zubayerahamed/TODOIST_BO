package com.zayaanit.todoist.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Users;
import com.zayaanit.todoist.entity.UsersWorkspaces;
import com.zayaanit.todoist.entity.Workspaces;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.repo.UsersRepo;
import com.zayaanit.todoist.repo.UsersWorkspacesRepo;
import com.zayaanit.todoist.repo.WorkspacesRepo;
import com.zayaanit.todoist.service.XusersService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class XusersServiceImpl implements XusersService {

	@Autowired private UsersRepo xusersRepo;
	@Autowired private UsersWorkspacesRepo xusersZbusinessRepo;
	@Autowired private WorkspacesRepo zbusinessRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("Email required");
		}

		Optional<Users> xusersOp = xusersRepo.findById(Long.valueOf(username));
		if(!xusersOp.isPresent()) throw new UsernameNotFoundException("User not exist.");

		Users xuser = xusersOp.get();
		if(Boolean.FALSE.equals(xuser.getIsActive())) {
			throw new UsernameNotFoundException("User inactive.");
		}

		Optional<UsersWorkspaces> xusersZbusinessOp = xusersZbusinessRepo.findByUserIdAndIsPrimary(xuser.getId(), Boolean.TRUE);
		if(!xusersZbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Optional<Workspaces> zbusinessOp = zbusinessRepo.findById(xusersZbusinessOp.get().getWorkspaceId());
		if(!zbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Workspaces zbusiness = zbusinessOp.get();
		if(Boolean.FALSE.equals(zbusiness.getIsActive())) {
			throw new UsernameNotFoundException("Workspace is disabled");
		}

		return new MyUserDetail(xuser, zbusiness);
	}

	@Override
	public Users createUser(Users xusers) {
		return xusersRepo.save(xusers);
	}

	
}

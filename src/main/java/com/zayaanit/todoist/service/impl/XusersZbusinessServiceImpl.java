package com.zayaanit.todoist.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.UsersWorkspaces;
import com.zayaanit.todoist.repo.UsersWorkspacesRepo;
import com.zayaanit.todoist.service.XusersZbusinessService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class XusersZbusinessServiceImpl implements XusersZbusinessService {

	@Autowired private UsersWorkspacesRepo xusersZbusinessRepo;

	@Override
	public List<UsersWorkspaces> findAllByZuser(Long zuser) {
		return xusersZbusinessRepo.findAllByUserId(zuser);
	}

	@Override
	public UsersWorkspaces findByZuserAndZprimary(Long zuser, Boolean zprimary) {
		Optional<UsersWorkspaces> xusersZbusinessOp = xusersZbusinessRepo.findByUserIdAndIsPrimary(zuser, zprimary);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	@Override
	public UsersWorkspaces findByZuserAndZadmin(Long zuser, Boolean zadmin) {
		Optional<UsersWorkspaces> xusersZbusinessOp = xusersZbusinessRepo.findByUserIdAndIsAdmin(zuser, zadmin);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	@Override
	public UsersWorkspaces findByZuserAndZcollaborator(Long zuser, Boolean zcollaborator) {
		Optional<UsersWorkspaces> xusersZbusinessOp = xusersZbusinessRepo.findByUserIdAndIsCollaborator(zuser, zcollaborator);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	

}

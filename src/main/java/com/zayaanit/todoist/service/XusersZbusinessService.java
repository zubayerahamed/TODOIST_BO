package com.zayaanit.todoist.service;

import java.util.List;

import com.zayaanit.todoist.entity.UsersWorkspaces;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public interface XusersZbusinessService {

	List<UsersWorkspaces> findAllByZuser(Long zuser);
	UsersWorkspaces findByZuserAndZprimary(Long zuser, Boolean zprimary);
	UsersWorkspaces findByZuserAndZadmin(Long zuser, Boolean zadmin);
	UsersWorkspaces findByZuserAndZcollaborator(Long zuser, Boolean zcollaborator);
}

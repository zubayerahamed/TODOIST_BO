package com.zayaanit.todoist.service;

import java.util.List;

import com.zayaanit.todoist.entity.XusersZbusiness;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public interface XusersZbusinessService {

	List<XusersZbusiness> findAllByZuser(Integer zuser);
	XusersZbusiness findByZuserAndZprimary(Integer zuser, Boolean zprimary);
	XusersZbusiness findByZuserAndZadmin(Integer zuser, Boolean zadmin);
	XusersZbusiness findByZuserAndZcollaborator(Integer zuser, Boolean zcollaborator);
}

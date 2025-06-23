package com.zayaanit.todoist.service;

import java.util.List;

import com.zayaanit.todoist.entity.XusersZbusiness;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
public interface XusersZbusinessService {

	List<XusersZbusiness> findAllByZuser(Long zuser);
	XusersZbusiness findByZuserAndZprimary(Long zuser, Boolean zprimary);
	XusersZbusiness findByZuserAndZadmin(Long zuser, Boolean zadmin);
	XusersZbusiness findByZuserAndZcollaborator(Long zuser, Boolean zcollaborator);
}

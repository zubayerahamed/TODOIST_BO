package com.zayaanit.todoist.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.XusersZbusiness;
import com.zayaanit.todoist.repo.XusersZbusinessRepo;
import com.zayaanit.todoist.service.XusersZbusinessService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class XusersZbusinessServiceImpl implements XusersZbusinessService {

	@Autowired private XusersZbusinessRepo xusersZbusinessRepo;

	@Override
	public List<XusersZbusiness> findAllByZuser(Long zuser) {
		return xusersZbusinessRepo.findAllByZuser(zuser);
	}

	@Override
	public XusersZbusiness findByZuserAndZprimary(Long zuser, Boolean zprimary) {
		Optional<XusersZbusiness> xusersZbusinessOp = xusersZbusinessRepo.findByZuserAndZprimary(zuser, zprimary);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	@Override
	public XusersZbusiness findByZuserAndZadmin(Long zuser, Boolean zadmin) {
		Optional<XusersZbusiness> xusersZbusinessOp = xusersZbusinessRepo.findByZuserAndZadmin(zuser, zadmin);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	@Override
	public XusersZbusiness findByZuserAndZcollaborator(Long zuser, Boolean zcollaborator) {
		Optional<XusersZbusiness> xusersZbusinessOp = xusersZbusinessRepo.findByZuserAndZcollaborator(zuser, zcollaborator);
		return xusersZbusinessOp.isPresent() ? xusersZbusinessOp.get() : null;
	}

	

}

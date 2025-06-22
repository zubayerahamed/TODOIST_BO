package com.zayaanit.todoist.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.entity.XusersZbusiness;
import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.model.MyUserDetail;
import com.zayaanit.todoist.repo.XusersRepo;
import com.zayaanit.todoist.repo.XusersZbusinessRepo;
import com.zayaanit.todoist.repo.ZbusinessRepo;
import com.zayaanit.todoist.service.XusersService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class XusersServiceImpl implements XusersService {

	@Autowired private XusersRepo xusersRepo;
	@Autowired private XusersZbusinessRepo xusersZbusinessRepo;
	@Autowired private ZbusinessRepo zbusinessRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("Email required");
		}

		Optional<Xusers> xusersOp = xusersRepo.findByZemail(username);
		if(!xusersOp.isPresent()) throw new UsernameNotFoundException("User not exist.");

		Xusers xuser = xusersOp.get();
		if(Boolean.FALSE.equals(xuser.getZactive())) {
			throw new UsernameNotFoundException("User inactive.");
		}

		Optional<XusersZbusiness> xusersZbusinessOp = xusersZbusinessRepo.findByZuserAndZprimary(xuser.getZuser(), Boolean.TRUE);
		if(!xusersZbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Optional<Zbusiness> zbusinessOp = zbusinessRepo.findById(xusersZbusinessOp.get().getZid());
		if(!zbusinessOp.isPresent()) {
			throw new UsernameNotFoundException("Primary workspace not found");
		}

		Zbusiness zbusiness = zbusinessOp.get();
		if(Boolean.FALSE.equals(zbusiness.getZactive())) {
			throw new UsernameNotFoundException("Workspace is disabled");
		}

		return new MyUserDetail(xuser, zbusiness);
	}

	@Override
	public Xusers createUser(Xusers xusers) {
		return xusersRepo.save(xusers);
	}

	
}

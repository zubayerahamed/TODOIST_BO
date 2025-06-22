package com.zayaanit.todoist.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.exception.ServiceException;
import com.zayaanit.todoist.repo.ZbusinessRepo;
import com.zayaanit.todoist.service.ZbusinessService;

/**
 * Zubayer Ahamed
 * @since Jun 22, 2025
 */
@Service
public class ZbusinessServiceImpl implements ZbusinessService {

	@Autowired private ZbusinessRepo zbusinessRepo;

	@Override
	public Zbusiness findById(Integer zid) throws ServiceException {
		if(zid == null) throw new ServiceException("Business id null");

		Optional<Zbusiness> zbusinessOp = zbusinessRepo.findById(zid);

		return zbusinessOp.isPresent() ? zbusinessOp.get() : null;
	}
	
	
}

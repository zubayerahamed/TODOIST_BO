package com.zayaanit.todoist.service;

import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.exception.ServiceException;

public interface ZbusinessService {

	Zbusiness findById(Long zid) throws ServiceException;
}

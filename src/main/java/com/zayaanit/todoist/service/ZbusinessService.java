package com.zayaanit.todoist.service;

import com.zayaanit.todoist.entity.Workspaces;
import com.zayaanit.todoist.exception.CustomException;

public interface ZbusinessService {

	Workspaces findById(Long zid) throws CustomException;
}

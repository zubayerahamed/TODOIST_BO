package com.zayaanit.todoist.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zayaanit.todoist.entity.Xusers;

public interface XusersService extends UserDetailsService {

	Xusers createUser(Xusers xusers);
}

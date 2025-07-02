package com.zayaanit.todoist.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zayaanit.todoist.entity.Users;

public interface XusersService extends UserDetailsService {

	Users createUser(Users xusers);
}

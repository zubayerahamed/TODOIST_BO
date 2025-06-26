package com.zayaanit.todoist.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.todoist.anno.RestApiController;
import com.zayaanit.todoist.dto.req.UsersReqDto;
import com.zayaanit.todoist.dto.res.UsersResDto;
import com.zayaanit.todoist.service.UsersService;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@RestApiController
@RequestMapping("/api/v1/users")
public class UsersController extends AbstractBaseController<UsersReqDto, UsersResDto, List<UsersResDto>, Page<UsersResDto>, Long> {

	public UsersController(UsersService<UsersReqDto, UsersResDto, List<UsersResDto>, Page<UsersResDto>, Long> service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

}

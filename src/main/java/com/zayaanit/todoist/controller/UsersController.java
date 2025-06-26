package com.zayaanit.todoist.controller;

import java.util.List;

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
@RequestMapping("/users")
public class UsersController extends AbstractBaseController<List<UsersResDto>, UsersReqDto, UsersResDto> {

	public UsersController(UsersService<List<UsersResDto>, UsersReqDto, UsersResDto> service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

}

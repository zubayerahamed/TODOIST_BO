package com.zayaanit.todoist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.dto.req.UsersReqDto;
import com.zayaanit.todoist.dto.res.UsersResDto;
import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.exception.CustomException;
import com.zayaanit.todoist.repo.XusersRepo;
import com.zayaanit.todoist.service.UsersService;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Service()
public class UsersServiceImpl implements UsersService<List<UsersResDto>, UsersReqDto, UsersResDto> {

	@Autowired XusersRepo xusersRepo;
	
	@Override
	public List<UsersResDto> getAll() throws CustomException {
		List<Xusers> list = xusersRepo.findAll();

		List<UsersResDto> resData = new ArrayList<>();
		list.stream().forEach(f -> {
			UsersResDto resDto = new UsersResDto();
			BeanUtils.copyProperties(f, resDto);
			resData.add(resDto);
		});

		return resData;
	}

	@Override
	public UsersResDto save(UsersReqDto req) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersResDto update(UsersReqDto req) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersResDto find(Object id) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Object id) throws CustomException {
		// TODO Auto-generated method stub
		
	}

}

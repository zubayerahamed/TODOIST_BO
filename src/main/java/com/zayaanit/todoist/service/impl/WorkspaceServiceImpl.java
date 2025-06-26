package com.zayaanit.todoist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.dto.req.WorkspaceReqDto;
import com.zayaanit.todoist.dto.res.WorkspaceResDto;
import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.exception.CustomException;
import com.zayaanit.todoist.repo.ZbusinessRepo;
import com.zayaanit.todoist.service.WorkspaceService;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService<List<WorkspaceResDto>, WorkspaceReqDto, WorkspaceResDto> {

	@Autowired private ZbusinessRepo zbusinessRepo;

	@Override
	public List<WorkspaceResDto> getAll() throws CustomException {
		List<Zbusiness> workspaces = zbusinessRepo.findAll();
		List<WorkspaceResDto> resData = new ArrayList<>();
		workspaces.stream().forEach(w -> {
			WorkspaceResDto resDto = new WorkspaceResDto();
			BeanUtils.copyProperties(w, resDto);
			resData.add(resDto);
		});
		return resData;
	}

	@Override
	public WorkspaceResDto save(WorkspaceReqDto req) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkspaceResDto update(WorkspaceReqDto req) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkspaceResDto find(Object id) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Object id) throws CustomException {
		// TODO Auto-generated method stub
		
	}

}

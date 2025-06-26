package com.zayaanit.todoist.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.dto.req.WorkspaceReqDto;
import com.zayaanit.todoist.dto.res.WorkspaceResDto;
import com.zayaanit.todoist.entity.Zbusiness;
import com.zayaanit.todoist.exception.CustomException;
import com.zayaanit.todoist.repo.ZbusinessRepo;
import com.zayaanit.todoist.service.WorkspaceService;

import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jun 26, 2025
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService<WorkspaceReqDto, WorkspaceResDto, List<WorkspaceResDto>, Page<WorkspaceResDto>, Long> {

	@Autowired private ZbusinessRepo zbusinessRepo;

	@Override
	public List<WorkspaceResDto> getAll() throws CustomException {
		List<Zbusiness> workspaces = zbusinessRepo.findAll();
		List<WorkspaceResDto> resData = new ArrayList<>();
		workspaces.stream().forEach(w -> {
			WorkspaceResDto resDto = new WorkspaceResDto(w);
			resData.add(resDto);
		});
		return resData;
	}

	@Override
	public Page<WorkspaceResDto> getAll(Pageable pageable) throws CustomException {
		Page<Zbusiness> workspacesPage = zbusinessRepo.findAll(pageable);

		Page<WorkspaceResDto> dtoPage = workspacesPage.map(entity -> {
			WorkspaceResDto dto = new WorkspaceResDto(entity);
			return dto;
		});

		return dtoPage;
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
	public WorkspaceResDto find(Long id) throws CustomException {
		Optional<Zbusiness> zbusinessOp = zbusinessRepo.findById(id);
		if(!zbusinessOp.isPresent()) throw new CustomException("Workspace not found", HttpStatus.BAD_REQUEST);
		return new WorkspaceResDto(zbusinessOp.get());
	}

	@Transactional
	@Override
	public void delete(Long id) throws CustomException {
		zbusinessRepo.deleteById(id);
	}

}

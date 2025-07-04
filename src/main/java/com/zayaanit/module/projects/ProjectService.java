package com.zayaanit.module.projects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@Service
public class ProjectService extends BaseService {

	@Autowired private ProjectRepo projectRepo;

	public List<ProjectResDto> getAll() {
		List<Project> projects = projectRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());
		if(projects.isEmpty()) return Collections.emptyList(); 

		List<ProjectResDto> responseData = new ArrayList<>();
		projects.stream().forEach(p -> {
			responseData.add(new ProjectResDto(p));
		});
		return responseData;
	}

	public ProjectResDto findById(Long id) throws CustomException {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);
		return new ProjectResDto(projectOp.get());
	}

	@Transactional
	public CreateProjectResDto create(CreateProjectReqDto reqDto) throws CustomException {
		Project project = reqDto.getBean();
		project.setWorkspaceId(loggedinUser().getWorkspace().getId());
		project.setSeqn(0);
		project.setIsSystemDefined(false);

		project = projectRepo.save(project);
		return new CreateProjectResDto(project);
	}

	@Transactional
	public UpdateProjectResDto update(UpdateProjectReqDto reqDto) throws CustomException {
		if(reqDto.getId() == null) throw new CustomException("Project id required", HttpStatus.BAD_REQUEST);

		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(reqDto.getId(), loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project existobj = projectOp.get();
		BeanUtils.copyProperties(reqDto, existobj);

		existobj = projectRepo.save(existobj);
		return new UpdateProjectResDto(existobj);
	}

	@Transactional
	public void deleteById(Long id) throws CustomException {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project project = projectOp.get();
		if(Boolean.TRUE.equals(project.getIsSystemDefined())) {
			throw new CustomException("System defined project can't be deleted", HttpStatus.BAD_REQUEST);
		}

		// Delete all sub task
		// Delete all task
		// Delete all sections
		// Delete all project settings
		// Delete project
		projectRepo.delete(projectOp.get());
	}

}

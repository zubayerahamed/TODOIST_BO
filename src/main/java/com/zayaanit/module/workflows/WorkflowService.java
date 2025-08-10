package com.zayaanit.module.workflows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.enums.ReferenceType;
import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.projects.Project;
import com.zayaanit.module.projects.ProjectRepo;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Aug 10, 2025
 */
@Service
public class WorkflowService extends BaseService {

	@Autowired private WorkflowRepo workflowRepo;
	@Autowired private ProjectRepo projectRepo;

	public List<WorkflowResDto> getAllWorkspaceWorkflows() {
		List<Workflow> workflows = workflowRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		if(workflows.isEmpty()) return Collections.emptyList(); 

		List<WorkflowResDto> responseData = new ArrayList<>();
		workflows.stream().forEach(c -> {
			responseData.add(new WorkflowResDto(c));
		});
		return responseData;
	}

	public List<WorkflowResDto> getAllProjectWorkflows(Long projectId) {
		List<Workflow> workflows = workflowRepo.findAllByReferenceIdAndReferenceType(projectId, ReferenceType.PROJECT);
		if(workflows.isEmpty()) return Collections.emptyList(); 

		List<WorkflowResDto> responseData = new ArrayList<>();
		workflows.stream().forEach(c -> {
			responseData.add(new WorkflowResDto(c));
		});
		return responseData;
	}

	public WorkflowResDto findById(Long id) throws CustomException {
		Optional<Workflow> workflowOp = workflowRepo.findById(id);
		if(!workflowOp.isPresent()) throw new CustomException("Workflow not exist", HttpStatus.NOT_FOUND);
		return new WorkflowResDto(workflowOp.get());
	}

	@Transactional
	public WorkflowResDto create(CreateWorkflowReqDto reqDto) throws CustomException {
		ReferenceType referenceType = null;
		if(reqDto.getReferenceId() == null || reqDto.getReferenceId() == 0) {
			referenceType = ReferenceType.WORKSPACE;
			reqDto.setReferenceId(loggedinUser().getWorkspace().getId());
		} else {
			// Valdate id with project id
			referenceType = ReferenceType.PROJECT;
			Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(reqDto.getReferenceId(), loggedinUser().getWorkspace().getId());
			if(!projectOp.isPresent()) {
				throw new CustomException("Reference id is not valid project id", HttpStatus.BAD_REQUEST);
			}
		}

		Workflow workflow = reqDto.getBean();
		workflow.setReferenceType(referenceType);
		workflow.setIsSystemDefined(false);
		workflow = workflowRepo.save(workflow);

		return new WorkflowResDto(workflow);
	}

	@Transactional
	public WorkflowResDto update(UpdateWorkflowReqDto reqDto) throws CustomException {
		Optional<Workflow> workflowOp = workflowRepo.findById(reqDto.getId());
		if(!workflowOp.isPresent()) {
			throw new CustomException("Status not exist", HttpStatus.BAD_REQUEST);
		}

		Workflow exisObj = workflowOp.get();
		BeanUtils.copyProperties(reqDto, exisObj);
		exisObj = workflowRepo.save(exisObj);

		return new WorkflowResDto(exisObj);
	}

	@Transactional
	public void deleteById(Long id) throws CustomException {
		Optional<Workflow> workflowOp = workflowRepo.findById(id);
		if(!workflowOp.isPresent()) {
			throw new CustomException("Workflow not exist", HttpStatus.BAD_REQUEST);
		}

		workflowRepo.delete(workflowOp.get());
	}
}

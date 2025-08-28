package com.zayaanit.module.projects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.enums.ReferenceType;
import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.category.Category;
import com.zayaanit.module.category.CategoryRepo;
import com.zayaanit.module.events.EventRepo;
import com.zayaanit.module.tasks.TaskRepo;
import com.zayaanit.module.workflows.Workflow;
import com.zayaanit.module.workflows.WorkflowRepo;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@Service
public class ProjectService extends BaseService {

	@Autowired private ProjectRepo projectRepo;
	@Autowired private CategoryRepo categoryRepo;
	@Autowired private WorkflowRepo workflowRepo;
	@Autowired private EventRepo eventRepo;
	@Autowired private TaskRepo taskRepo;

	public List<ProjectResDto> getAll() {
		List<Project> projects = projectRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());
		if(projects.isEmpty()) return Collections.emptyList(); 

		List<ProjectResDto> responseData = new ArrayList<>();
		projects.stream().forEach(p -> {
			ProjectResDto prd = new ProjectResDto(p);
			// add total events
			prd.setTotalActiveEvents(eventRepo.countProjectActiveEvents(p.getId()));
			// add total tasks
			prd.setTotalActiveTasks(taskRepo.countProjectActiveTasks(p.getId()));
			responseData.add(prd);
		});
		return responseData;
	}

	public long totalEventAndTasksCount(Long id) throws CustomException {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		long totalEvents = eventRepo.countProjectActiveEvents(id);
		long totalTasks = taskRepo.countProjectActiveTasks(id);

		return totalEvents + totalTasks;
	}

	public ProjectResDto findById(Long id) throws CustomException {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);
		return new ProjectResDto(projectOp.get());
	}

	@Transactional
	public CreateProjectResDto create(CreateProjectReqDto reqDto) throws CustomException {
		// Project reserved name validation
		if("INBOX".equalsIgnoreCase(reqDto.getName())) {
			throw new CustomException("Projec name Inbox is resereved for system", HttpStatus.BAD_REQUEST);
		}

		Project project = reqDto.getBean();
		project.setWorkspaceId(loggedinUser().getWorkspace().getId());
		project.setSeqn(0);
		project.setIsSystemDefined(false);
		project.setIsInheritSettings(true);
		project = projectRepo.save(project);

		// Partially need to create project settings also
		// Workflow duplication from workspace
		List<Workflow> workflows =  workflowRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		if(workflows.isEmpty()) {
			throw new CustomException("Data missing! Workspace don't have system defiend workflow.", HttpStatus.NOT_FOUND);
		}

		for(Workflow w : workflows) {
			Workflow projectWorkflow = Workflow.builder()
					.referenceId(project.getId())
					.referenceType(ReferenceType.PROJECT)
					.name(w.getName())
					.isSystemDefined(w.getIsSystemDefined())
					.seqn(w.getSeqn())
					.color(w.getColor())
					.isInherited(true)
					.parentId(w.getId())
					.build();
			workflowRepo.save(projectWorkflow);
		}

		// Create a system defined workflow fo this project which is not inherited
		Workflow projectWorkflow = Workflow.builder()
				.referenceId(project.getId())
				.referenceType(ReferenceType.PROJECT)
				.name("Completed")
				.isSystemDefined(true)
				.seqn(999)
				.color("#000000")
				.isInherited(false)
				.parentId(null)
				.build();
		workflowRepo.save(projectWorkflow);

		// Now duplicate all the categories
		List<Category> categories = categoryRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		for(Category c : categories) {
			Category category = Category.builder()
					.referenceId(project.getId())
					.referenceType(ReferenceType.PROJECT)
					.name(c.getName())
					.color(c.getColor())
					.isForTask(c.getIsForTask())
					.isForEvent(c.getIsForEvent())
					.seqn(c.getSeqn())
					.isDefaultForTask(c.getIsDefaultForTask())
					.isDefaultForEvent(c.getIsDefaultForEvent())
					.isInherited(true)
					.parentId(c.getId())
					.build();
			categoryRepo.save(category);
		}

		return new CreateProjectResDto(project);
	}

	@Transactional
	public UpdateProjectResDto update(UpdateProjectReqDto reqDto) throws CustomException {
		// Project reserved name validation
		if("INBOX".equalsIgnoreCase(reqDto.getName())) {
			throw new CustomException("Projec name Inbox is resereved for system", HttpStatus.BAD_REQUEST);
		}

		if(reqDto.getId() == null) throw new CustomException("Project id required", HttpStatus.BAD_REQUEST);

		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(reqDto.getId(), loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project existobj = projectOp.get();
		existobj.setName(reqDto.getName());
		if(StringUtils.isNotBlank(reqDto.getColor())) {
			existobj.setColor(reqDto.getColor());
		}
		if(reqDto.getIsFavourite() != null) {
			existobj.setIsFavourite(reqDto.getIsFavourite());
		}
		if(reqDto.getLayoutType() != null) {
			existobj.setLayoutType(reqDto.getLayoutType());
		}
		existobj = projectRepo.save(existobj);

		return new UpdateProjectResDto(existobj);
	}

	@Transactional
	public ProjectResDto addToFavourite(Long id) {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project existobj = projectOp.get();
		existobj.setIsFavourite(true);
		existobj = projectRepo.save(existobj);

		return new ProjectResDto(existobj);
	}

	@Transactional
	public void disableInheritWorkspaceSettings(Long id) {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project project = projectOp.get();
		project.setIsInheritSettings(false);
		projectRepo.save(project);
	}

	@Transactional
	public void inheritWorkspaceSettings(Long id) {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project project = projectOp.get();
		project.setIsInheritSettings(true);
		projectRepo.save(project);

		// Copy categories
		List<Category> categories = categoryRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		if(!categories.isEmpty()) {
			categories.stream().forEach(c -> {
				Category category = new Category();

				// check the category already exist, so then just update it, so that it can align with workspace
				Optional<Category> existCategoryOp = categoryRepo.findByReferenceIdAndReferenceTypeAndParentId(id, ReferenceType.PROJECT, c.getId());
				if(existCategoryOp.isPresent()) category = existCategoryOp.get();

				category.setReferenceId(id);
				category.setReferenceType(ReferenceType.PROJECT);
				category.setName(c.getName());
				category.setColor(c.getColor());
				category.setIsForTask(c.getIsForTask());
				category.setIsForEvent(c.getIsForEvent());
				category.setSeqn(c.getSeqn());
				category.setIsDefaultForTask(c.getIsDefaultForTask());
				category.setIsDefaultForEvent(c.getIsDefaultForEvent());
				category.setIsInherited(true);
				category.setParentId(c.getId());
				categoryRepo.save(category);
			});
		}


		// Copy workflows without the completed, because completed already created
		List<Workflow> workflows = workflowRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		if(!workflows.isEmpty()) {
			workflows.stream().forEach(w -> {
				Workflow workflow = new Workflow();

				// check the inherited already exist, then just update it again, so that it is align with workspace
				Optional<Workflow> existWorkflowOp = workflowRepo.findByReferenceIdAndReferenceTypeAndParentId(id, ReferenceType.PROJECT, w.getId());
				if(existWorkflowOp.isPresent()) workflow = existWorkflowOp.get();

				workflow.setReferenceId(id);
				workflow.setReferenceType(ReferenceType.PROJECT);
				workflow.setName(w.getName());
				workflow.setIsSystemDefined(w.getIsSystemDefined());
				workflow.setSeqn(w.getSeqn());
				workflow.setColor(w.getColor());
				workflow.setIsInherited(true);
				workflow.setParentId(w.getId());
				workflowRepo.save(workflow);

			});
		}

	}

	@Transactional
	public ProjectResDto removeFromFavourite(Long id) {
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(id, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) throw new CustomException("Project not exist", HttpStatus.NOT_FOUND);

		Project existobj = projectOp.get();
		existobj.setIsFavourite(false);
		existobj = projectRepo.save(existobj);

		return new ProjectResDto(existobj);
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

	public Page<ProjectResDto> getAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Project> projectPage = projectRepo.findAllByWorkspaceId(
				loggedinUser().getWorkspace().getId(), pageable
		);

		return projectPage.map(ProjectResDto::new);
	}

}

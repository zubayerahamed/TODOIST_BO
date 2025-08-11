package com.zayaanit.module.category;

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
 * @since Jul 3, 2025
 */
@Service
public class CategoryService extends BaseService {

	@Autowired private CategoryRepo categoryRepo;
	@Autowired private ProjectRepo projectRepo;

	public List<CategoryResDto> getAllWorkspaceCategories() {
		List<Category> categories = categoryRepo.findAllByReferenceIdAndReferenceType(loggedinUser().getWorkspace().getId(), ReferenceType.WORKSPACE);
		if(categories.isEmpty()) return Collections.emptyList(); 

		List<CategoryResDto> responseData = new ArrayList<>();
		categories.stream().forEach(c -> {
			responseData.add(new CategoryResDto(c));
		});
		return responseData;
	}

	public List<CategoryResDto> getAllProjectCategories(Long projectId) throws CustomException {
		// Get the project record first to check the inheritence
		Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(projectId, loggedinUser().getWorkspace().getId());
		if(!projectOp.isPresent()) {
			throw new CustomException("Project not eixsit", HttpStatus.NOT_FOUND);
		}

		List<Category> categories = new ArrayList<>();
		Project project = projectOp.get();
		if(Boolean.TRUE.equals(project.getIsInheritSettings())) {
			categories = categoryRepo.findAllByReferenceIdAndReferenceTypeAndIsInheritedTrue(projectId, ReferenceType.PROJECT);
		} else {
			categories = categoryRepo.findAllByReferenceIdAndReferenceTypeAndIsInheritedFalse(projectId, ReferenceType.PROJECT);
		}

		if(categories.isEmpty()) return Collections.emptyList(); 

		List<CategoryResDto> responseData = new ArrayList<>();
		categories.stream().forEach(c -> {
			responseData.add(new CategoryResDto(c));
		});
		return responseData;
	}

	public CategoryResDto findById(Long id) throws CustomException {
		Optional<Category> categoryOp = categoryRepo.findById(id);
		if(!categoryOp.isPresent()) throw new CustomException("Category not exist", HttpStatus.NOT_FOUND);
		return new CategoryResDto(categoryOp.get());
	}

	@Transactional
	public CategoryResDto create(CreateCategoryReqDto reqDto) throws CustomException {
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
			if(Boolean.TRUE.equals(projectOp.get().getIsInheritSettings())) {
				throw new CustomException("Project settings inherited from workspace is enabled! Disable it first.", HttpStatus.BAD_REQUEST);
			}
		}

		Category category = reqDto.getBean();
		category.setReferenceType(referenceType);
		category.setSeqn(0);
		category.setIsDefaultForEvent(false);
		category.setIsDefaultForTask(false);
		category.setIsInherited(false);
		category.setParentId(null);
		category = categoryRepo.save(category);

		return new CategoryResDto(category);
	}

	@Transactional
	public CategoryResDto update(UpdateCategoryReqDto reqDto) throws CustomException {
		Optional<Category> categoryOp = categoryRepo.findById(reqDto.getId());
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}

		Category exisObj = categoryOp.get();
		BeanUtils.copyProperties(reqDto, exisObj);
		// Default task or event validation
		if(Boolean.FALSE.equals(exisObj.getIsForTask())) {
			exisObj.setIsDefaultForTask(false);
		}
		if(Boolean.FALSE.equals(exisObj.getIsForEvent())) {
			exisObj.setIsDefaultForEvent(false);
		}
		exisObj = categoryRepo.save(exisObj);

		// check all the inherited childs and update their data too
		List<Category> categories = categoryRepo.findAllByParentIdAndIsInheritedTrue(exisObj.getId());
		for(Category category : categories) {
			category.setName(exisObj.getName());
			category.setColor(exisObj.getColor());
			categoryRepo.save(category);
		}

		return new CategoryResDto(exisObj);
	}

	@Transactional
	public void deleteById(Long id) throws CustomException {
		Optional<Category> categoryOp = categoryRepo.findById(id);
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}
		categoryRepo.delete(categoryOp.get());

		// check all the childs and make their inheritance false and update parent reference to null
		List<Category> categories = categoryRepo.findAllByParentIdAndIsInheritedTrue(id);
		for(Category category : categories) {
			category.setIsInherited(false);
			category.setParentId(null);
			categoryRepo.save(category);
		}
	}

	@Transactional
	public CategoryResDto addToDefaultTask(Long referenceid, Long id) throws CustomException {
		ReferenceType referenceType = ReferenceType.PROJECT;
		if(referenceid == null || referenceid == 0) {
			referenceid = loggedinUser().getWorkspace().getId();
			referenceType = ReferenceType.WORKSPACE;
		}

		Optional<Category> categoryOp = categoryRepo.findById(id);
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}

		// Remove existing default first
		if(referenceType == ReferenceType.WORKSPACE) {
			Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrue(referenceid, referenceType);
			if(existingDefaultOp.isPresent()) {
				Category existingDefaultCategory = existingDefaultOp.get();
				existingDefaultCategory.setIsDefaultForTask(false);
				categoryRepo.save(existingDefaultCategory);
			}
		} else {
			// get project inheritence info first
			Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(referenceid, loggedinUser().getWorkspace().getId());
			if(!projectOp.isPresent()) throw new CustomException("Project not eixist", HttpStatus.NOT_FOUND);

			Project project = projectOp.get();

			if(Boolean.TRUE.equals(project.getIsInheritSettings())) {
				Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrueAndIsInheritedTrue(referenceid, referenceType);
				if(existingDefaultOp.isPresent()) {
					Category existingDefaultCategory = existingDefaultOp.get();
					existingDefaultCategory.setIsDefaultForTask(false);
					categoryRepo.save(existingDefaultCategory);
				}
			} else {
				Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForTaskTrueAndIsInheritedFalse(referenceid, referenceType);
				if(existingDefaultOp.isPresent()) {
					Category existingDefaultCategory = existingDefaultOp.get();
					existingDefaultCategory.setIsDefaultForTask(false);
					categoryRepo.save(existingDefaultCategory);
				}
			}
		}

		Category exisObj = categoryOp.get();
		exisObj.setIsDefaultForTask(true);
		exisObj = categoryRepo.save(exisObj);

		return new CategoryResDto(exisObj);
	}

	@Transactional
	public CategoryResDto addToDefaultEvent(Long referenceid, Long id) throws CustomException {
		ReferenceType referenceType = ReferenceType.PROJECT;
		if(referenceid == null || referenceid == 0) {
			referenceid = loggedinUser().getWorkspace().getId();
			referenceType = ReferenceType.WORKSPACE;
		}

		Optional<Category> categoryOp = categoryRepo.findById(id);
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}

		// Remove existing default first
		if(referenceType == ReferenceType.WORKSPACE) {
			Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrue(referenceid, referenceType);
			if(existingDefaultOp.isPresent()) {
				Category existingDefaultCategory = existingDefaultOp.get();
				existingDefaultCategory.setIsDefaultForEvent(false);
				categoryRepo.save(existingDefaultCategory);
			}
		} else {
			// get project inheritence info first
			Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(referenceid, loggedinUser().getWorkspace().getId());
			if(!projectOp.isPresent()) throw new CustomException("Project not eixist", HttpStatus.NOT_FOUND);

			Project project = projectOp.get();

			if(Boolean.TRUE.equals(project.getIsInheritSettings())) {
				Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrueAndIsInheritedTrue(referenceid, referenceType);
				if(existingDefaultOp.isPresent()) {
					Category existingDefaultCategory = existingDefaultOp.get();
					existingDefaultCategory.setIsDefaultForEvent(false);
					categoryRepo.save(existingDefaultCategory);
				}
			} else {
				Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForEventTrueAndIsInheritedFalse(referenceid, referenceType);
				if(existingDefaultOp.isPresent()) {
					Category existingDefaultCategory = existingDefaultOp.get();
					existingDefaultCategory.setIsDefaultForEvent(false);
					categoryRepo.save(existingDefaultCategory);
				}
			}
		}

		Category exisObj = categoryOp.get();
		exisObj.setIsDefaultForEvent(true);
		exisObj = categoryRepo.save(exisObj);

		return new CategoryResDto(exisObj);
	}
}

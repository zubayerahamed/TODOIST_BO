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
		List<Category> categories = categoryRepo.findAllByReferenceId(loggedinUser().getWorkspace().getId());
		if(categories.isEmpty()) return Collections.emptyList(); 

		List<CategoryResDto> responseData = new ArrayList<>();
		categories.stream().forEach(c -> {
			responseData.add(new CategoryResDto(c));
		});
		return responseData;
	}

	public List<CategoryResDto> getAllProjectCategories(Long projectId) {
		List<Category> categories = categoryRepo.findAllByReferenceId(projectId);
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
	public CreateCategoryResDto create(CreateCategoryReqDto reqDto) throws CustomException {
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

		Category category = reqDto.getBean();
		category.setSeqn(0);
		category.setIsDefaultForEvent(false);
		category.setIsDefaultForTask(false);
		category.setReferenceType(referenceType);

		category = categoryRepo.save(category);
		return new CreateCategoryResDto(category);
	}

	@Transactional
	public UpdateCategoryResDto update(UpdateCategoryReqDto reqDto) throws CustomException {
		Optional<Category> categoryOp = categoryRepo.findById(reqDto.getId());
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}

		Category exisObj = categoryOp.get();
		BeanUtils.copyProperties(reqDto, exisObj);

		exisObj = categoryRepo.save(exisObj);
		return new UpdateCategoryResDto(exisObj);
	}

	@Transactional
	public void deleteById(Long id) throws CustomException {
		Optional<Category> categoryOp = categoryRepo.findById(id);
		if(!categoryOp.isPresent()) {
			throw new CustomException("Category not exist", HttpStatus.BAD_REQUEST);
		}

		categoryRepo.delete(categoryOp.get());
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
		Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForTask(referenceid, referenceType, Boolean.TRUE);
		if(existingDefaultOp.isPresent()) {
			Category existingDefaultCategory = existingDefaultOp.get();
			existingDefaultCategory.setIsDefaultForTask(false);
			categoryRepo.save(existingDefaultCategory);
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
		Optional<Category> existingDefaultOp = categoryRepo.findByReferenceIdAndReferenceTypeAndIsDefaultForEvent(referenceid, referenceType, Boolean.TRUE);
		if(existingDefaultOp.isPresent()) {
			Category existingDefaultCategory = existingDefaultOp.get();
			existingDefaultCategory.setIsDefaultForEvent(false);
			categoryRepo.save(existingDefaultCategory);
		}

		Category exisObj = categoryOp.get();
		exisObj.setIsDefaultForEvent(true);
		exisObj = categoryRepo.save(exisObj);

		return new CategoryResDto(exisObj);
	}
}

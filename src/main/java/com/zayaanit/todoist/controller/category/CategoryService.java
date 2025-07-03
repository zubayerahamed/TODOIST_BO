package com.zayaanit.todoist.controller.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.todoist.controller.BaseService;
import com.zayaanit.todoist.controller.projects.Project;
import com.zayaanit.todoist.controller.projects.ProjectRepo;
import com.zayaanit.todoist.exception.CustomException;

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
	public CreateCategoryResDto createCategory(CreateCategoryReqDto reqDto) throws CustomException {
		if(reqDto.getReferenceId() == null) {
			reqDto.setReferenceId(loggedinUser().getWorkspace().getId());
		} else {
			// Valdate id with project id
			Optional<Project> projectOp = projectRepo.findByIdAndWorkspaceId(reqDto.getReferenceId(), loggedinUser().getWorkspace().getId());
			if(!projectOp.isPresent()) {
				throw new CustomException("Reference id is not valid project id", HttpStatus.BAD_REQUEST);
			}
		}

		Category category = reqDto.getBean();
		category.setSeqn(0);

		category = categoryRepo.save(category);
		return new CreateCategoryResDto(category);
	}

	@Transactional
	public UpdateCategoryResDto updateCategory(UpdateCategoryReqDto reqDto) throws CustomException {
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
}

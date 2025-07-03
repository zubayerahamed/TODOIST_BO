package com.zayaanit.module.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

/**
 * Zubayer Ahamed
 * @since Jul 2, 2025
 */
@RestApiController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired private CategoryService categoryService;

	@GetMapping("/all/workspace")
	public ResponseEntity<SuccessResponse<List<CategoryResDto>>> getAllWorkspaceCategories(){
		List<CategoryResDto> resData = categoryService.getAllWorkspaceCategories();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/all/project/{id}")
	public ResponseEntity<SuccessResponse<List<CategoryResDto>>> getAllProjectCategories(@PathVariable Long id){
		List<CategoryResDto> resData = categoryService.getAllProjectCategories(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<CategoryResDto>> findCategory(@PathVariable Long id){
		CategoryResDto resData = categoryService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<CreateCategoryResDto>> createCategory(@Valid @RequestBody CreateCategoryReqDto reqDto){
		CreateCategoryResDto resData = categoryService.createCategory(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateCategoryResDto>> updateCategory(@RequestBody UpdateCategoryReqDto reqDto){
		UpdateCategoryResDto resData = categoryService.updateCategory(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<CategoryResDto>> deleteCategory(@PathVariable Long id){
		categoryService.deleteById(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_SUCCESS, null);
	}
}

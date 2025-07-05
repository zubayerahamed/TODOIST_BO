package com.zayaanit.module.users.preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @since Jul 5, 2025
 */
@RestApiController
@RequestMapping("/api/v1/preferences")
public class UserPreferenceController {

	@Autowired private UserPreferenceService userPreferenceService;

	@GetMapping
	public ResponseEntity<SuccessResponse<UserPreferenceResDto>> getPreference(){
		UserPreferenceResDto resData = userPreferenceService.getLoggedinUserPreference();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<UpdateUserPreferenceResDto>> update(@Valid @RequestBody UpdateUserPreferenceReqDto reqDto){
		UpdateUserPreferenceResDto resData = userPreferenceService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}
}

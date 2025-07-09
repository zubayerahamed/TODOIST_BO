package com.zayaanit.module.users.preferences;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 5, 2025
 */
@Service
public class UserPreferenceService extends BaseService {

	@Autowired private UserPreferenceRepo userPreferenceRepo;

	public UserPreferenceResDto getLoggedinUserPreference() throws CustomException {
		Optional<UserPreference> userPreferenceOp = userPreferenceRepo.findById(loggedinUser().getUserId());
		if(!userPreferenceOp.isPresent()) throw new CustomException("Preference data not found", HttpStatus.NOT_FOUND);
		return new UserPreferenceResDto(userPreferenceOp.get());
	}

	@Transactional
	public UpdateUserPreferenceResDto update(UpdateUserPreferenceReqDto reqDto) throws CustomException {
		Optional<UserPreference> userPreferenceOp = userPreferenceRepo.findById(loggedinUser().getUserId());
		if(!userPreferenceOp.isPresent()) throw new CustomException("Preference data not found", HttpStatus.NOT_FOUND);

		UserPreference existobj = userPreferenceOp.get();
		BeanUtils.copyProperties(reqDto, existobj);
		userPreferenceRepo.save(existobj);
		return new UpdateUserPreferenceResDto(existobj);
	}
}

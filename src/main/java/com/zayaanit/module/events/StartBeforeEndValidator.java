package com.zayaanit.module.events;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 14, 2025
 */
public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEndTime, CreateEventReqDto> {

	@Override
	public boolean isValid(CreateEventReqDto dto, ConstraintValidatorContext context) {
		if (dto.getStartTime() == null || dto.getEndTime() == null) {
			return true; // Let @NotNull handle null checks
		}
		return dto.getStartTime().isBefore(dto.getEndTime());
	}
}

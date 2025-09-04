package com.zayaanit.module.events;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 14, 2025
 */
public class CreateEventRequestValidator implements ConstraintValidator<ValidCreateEventRequest, CreateEventReqDto> {

	@Override
	public boolean isValid(CreateEventReqDto dto, ConstraintValidatorContext context) {
		boolean isValid = true;

		// Disable default constraint message
		context.disableDefaultConstraintViolation();

		// 1. Validate startTime < endTime
		if (dto.getStartTime() != null && dto.getEndTime() != null && !dto.getStartTime().isBefore(dto.getEndTime())) {
			context.buildConstraintViolationWithTemplate("Start time must be before end time")
			.addPropertyNode("startTime")
			.addConstraintViolation();
			isValid = false;
		}

		// 2. Validate reminder logic
		if (Boolean.TRUE.equals(dto.getIsReminderEnabled()) && (dto.getReminderBefore() == null || dto.getReminderBefore() < 0)) {
			context.buildConstraintViolationWithTemplate("Reminder time must be non-negative when reminder is enabled")
			.addPropertyNode("reminderBefore")
			.addConstraintViolation();
			isValid = false;
		}

		// 3. Validate eventDate not in the past
		if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDate.now())) {
			context.buildConstraintViolationWithTemplate("Event date cannot be in the past")
			.addPropertyNode("eventDate")
			.addConstraintViolation();
			isValid = false;
		}

		// 4. Event Repeater Id
		if(dto.getEventType() != null && EventType.REPEAT.equals(dto.getEventType())) {
			if(dto.getEventRepeaterId() == null) {
				context.buildConstraintViolationWithTemplate("Repeater reference required")
				.addPropertyNode("eventRepeaterId")
				.addConstraintViolation();
				isValid = false;
			}
		}

		return isValid;
	}
}

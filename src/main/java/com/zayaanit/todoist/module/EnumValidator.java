package com.zayaanit.todoist.module;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

	private List<String> acceptedValues;

	@Override
	public void initialize(ValidEnum annotation) {
		acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
				.map(Enum::name)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || acceptedValues.contains(value);
	}
}

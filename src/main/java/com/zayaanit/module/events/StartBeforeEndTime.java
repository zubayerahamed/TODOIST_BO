package com.zayaanit.module.events;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 14, 2025
 */
@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEndTime {

	String message() default "Event start time must be before event end time";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

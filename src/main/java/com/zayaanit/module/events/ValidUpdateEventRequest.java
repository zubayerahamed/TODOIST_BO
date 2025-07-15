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
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UpdateEventRequestValidator.class)
public @interface ValidUpdateEventRequest {

	String message() default "Invalid event update request";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

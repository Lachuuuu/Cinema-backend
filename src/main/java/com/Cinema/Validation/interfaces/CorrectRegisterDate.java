package com.Cinema.Validation.interfaces;

import com.Cinema.Validation.validators.CorrectRegisterDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CorrectRegisterDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectRegisterDate {
   String message() default "wrong birth date";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}
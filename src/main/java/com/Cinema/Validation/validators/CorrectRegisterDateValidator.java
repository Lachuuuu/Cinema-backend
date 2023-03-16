package com.Cinema.Validation.validators;

import com.Cinema.Validation.interfaces.CorrectRegisterDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CorrectRegisterDateValidator implements ConstraintValidator<CorrectRegisterDate, LocalDate> {
   @Override
   public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
      return date.atStartOfDay().isBefore(LocalDateTime.now());
   }

   @Override
   public void initialize(CorrectRegisterDate correctRegisterDate) {
      ConstraintValidator.super.initialize(correctRegisterDate);
   }

}

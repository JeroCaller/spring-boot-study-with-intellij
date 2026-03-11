package com.jerocaller.AuthTokenSecurity.validator.impl;

import com.jerocaller.AuthTokenSecurity.validator.annotation.NullableSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableSizeValidator implements ConstraintValidator<NullableSize, String> {
    private int min, max;

    @Override
    public void initialize(NullableSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        value = value.trim();
        return value.length() >= min && value.length() <= max;
    }
}

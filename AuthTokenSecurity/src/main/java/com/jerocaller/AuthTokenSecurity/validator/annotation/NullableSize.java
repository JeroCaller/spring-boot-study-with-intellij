package com.jerocaller.AuthTokenSecurity.validator.annotation;

import com.jerocaller.AuthTokenSecurity.validator.impl.NullableSizeValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     null 값은 허용하나, null이 아닌 경우 특정 길이 범위를 만족시켜야할 때 사용하는
 *     유효성 검증 어노테이션.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullableSizeValidator.class)
public @interface NullableSize {
    String message() default "길이가 범위에서 벗어납니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    int min() default 0;
    int max() default Integer.MAX_VALUE;
}

package com.thvnhng.mockproject.Valid.Annotation;

import com.thvnhng.mockproject.Valid.UserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUser {
    String message() default "User is already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
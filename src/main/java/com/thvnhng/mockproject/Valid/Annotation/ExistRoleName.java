package com.thvnhng.mockproject.Valid.Annotation;

import com.thvnhng.mockproject.Valid.RoleNameValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleNameValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistRoleName {

    String message() default "Role name is not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

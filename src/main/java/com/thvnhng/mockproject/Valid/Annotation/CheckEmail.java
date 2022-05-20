package com.thvnhng.mockproject.Valid.Annotation;

import com.thvnhng.mockproject.Valid.EmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckEmail {

    String message() default "Email is already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

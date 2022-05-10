package com.thvnhng.mockproject.Valid.Annotation;

import com.thvnhng.mockproject.Valid.CourseValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CourseValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckCourseName {

    String message() default "This course already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

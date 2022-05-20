package com.thvnhng.mockproject.Valid.Annotation;

import com.thvnhng.mockproject.Valid.SubjectNameValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SubjectNameValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSubjectName {

    String message() default "This subject name already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

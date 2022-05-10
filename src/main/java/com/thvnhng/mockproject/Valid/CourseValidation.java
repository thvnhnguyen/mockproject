package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.Valid.Annotation.CheckCourseName;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class CourseValidation implements ConstraintValidator<CheckCourseName, String> {

    private final CourseService courseService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !courseService.isExistCourse(s);
    }
}

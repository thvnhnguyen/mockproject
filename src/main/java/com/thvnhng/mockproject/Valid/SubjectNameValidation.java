package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.Service.SubjectService;
import com.thvnhng.mockproject.Valid.Annotation.CheckCourseName;
import com.thvnhng.mockproject.Valid.Annotation.UniqueSubjectName;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class SubjectNameValidation implements ConstraintValidator<UniqueSubjectName, String> {

    private final SubjectService subjectService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !subjectService.isExistSubjectName(s);
    }

}

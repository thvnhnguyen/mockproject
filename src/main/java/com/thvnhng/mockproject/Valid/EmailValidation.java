package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidation implements ConstraintValidator<CheckEmail, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean rs = s.contains("@gmail.com");
        return rs;
    }
}

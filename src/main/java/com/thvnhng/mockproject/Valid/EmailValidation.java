package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class EmailValidation implements ConstraintValidator<CheckEmail, String> {

    private final UserService userService;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.checkExistEmail(s);
    }
}

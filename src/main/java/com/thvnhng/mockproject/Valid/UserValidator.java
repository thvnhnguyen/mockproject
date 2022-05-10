package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.Valid.Annotation.CheckUser;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UserValidator implements
        ConstraintValidator<CheckUser, String> {

    private final UserService userService;
    @Override
    public void initialize(CheckUser username) {
    }

    @Override
    public boolean isValid(String username,
                           ConstraintValidatorContext cxt) {
        return !userService.checkExistUsername(username);
    }

}
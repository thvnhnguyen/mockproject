package com.thvnhng.mockproject.Exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements
        ConstraintValidator<CheckUser, String> {



    @Override
    public void initialize(CheckUser contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        //logic check user
        return false;
    }

}
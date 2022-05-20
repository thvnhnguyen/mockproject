package com.thvnhng.mockproject.Valid;

import com.thvnhng.mockproject.Service.RoleService;
import com.thvnhng.mockproject.Valid.Annotation.ExistRoleName;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class RoleNameValidation implements ConstraintValidator<ExistRoleName, String> {

    private final RoleService roleService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return roleService.isExistByRoleName(s);
    }

}

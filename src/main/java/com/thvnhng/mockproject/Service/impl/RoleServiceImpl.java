package com.thvnhng.mockproject.Service.impl;

import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Service.RoleService;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserService userService;
    @Override
    public Boolean isExistByRoleName(String roleName) {
        if (roleName == null) {
            return false;
        }
        List<String> strRoleName = new ArrayList<>();
        strRoleName.add(roleName);
        List<ERoles> eRolesList = new ArrayList<>();
        userService.validUserRoleList(strRoleName, eRolesList);
        return eRolesList.size() > 0;
    }

}

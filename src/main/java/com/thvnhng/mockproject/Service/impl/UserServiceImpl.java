package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Roles;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Boolean checkExistUsernameAndEmail(String username, String email) {
        return userRepository.existsByUsernameAndEmail(username, email);
    }

    @Override
    public Boolean checkExistId(Long id) {
        return userRepository.existsUsersById(id);
    }

    @Override
    public Boolean checkExistUsername(String username) {
        return userRepository.existsUsersByUsername(username);
    }

    @Override
    public Boolean checkExistEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Override
    public UserDTO detail(Long id) {
        return objectMapper.convertValue(userRepository.findById(id), UserDTO.class);
    }

    @Override
    public UserDTO updateUserInfo(UserDTO userDTO) {
        Users user = objectMapper.convertValue(userDTO, Users.class);
        List<Roles> rolesList = userRepository.findByUsernameAndStatus(userDTO.getUsername(), userDTO.getStatus()).get().getRolesList();
        user.setRolesList(rolesList);
        return objectMapper.convertValue(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO updateUserRoles(UserDTO userDTO) {
        Users user = objectMapper.convertValue(
                userRepository.findByUsernameAndStatus(userDTO.getUsername(), userDTO.getStatus()),
                Users.class
        );
        List<String> strNewRoles = userDTO.getRoleDTOList();
        List<ERoles> NewRolesList = new ArrayList<>();
        for (String strRole : strNewRoles) {
            if (strRole.equals(ERoles.ROLE_ADMIN.name())) {
                NewRolesList.add(ERoles.ROLE_ADMIN);
            } else if (strRole.equals(ERoles.ROLE_STUDENT.name())) {
                NewRolesList.add(ERoles.ROLE_STUDENT);
            }
        }
        List<Roles> newRoles = roleRepository.findByRoleNameIn(NewRolesList);
        user.setRolesList(newRoles);
        return objectMapper.convertValue(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO setStatus(Long id, Integer status) {
        if (userRepository.findById(id).isPresent()) {
            Users user = userRepository.findById(id).get();
            user.setStatus(status);
            return objectMapper.convertValue(userRepository.save(user), UserDTO.class);
        }
        return null;
    }

    @Override
    public List<UserDTO> listALl() {
        List<Users> usersList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(objectMapper.convertValue(user, UserDTO.class));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listUserByStatus(Integer status) {
        List<Users> usersList = userRepository.findAllByStatus(status);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(objectMapper.convertValue(user, UserDTO.class));
        }
        return userDTOList;
    }

}

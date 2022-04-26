package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Roles;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder encoder;

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
        List<Roles> rolesList = userRepository.findByUsername(userDTO.getUsername()).getRolesList();
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setRolesList(rolesList);
        userRepository.save(user);
        return userDTO;
    }

    @Override
    public UserDTO updateUserRoles(UserDTO userDTO) {
        Users user = userRepository.findByUsername(userDTO.getUsername());
        List<String> strNewRoles = userDTO.getRoleList();
        List<ERoles> newRolesList = new ArrayList<>();
        for (String strRole : strNewRoles) {
            if (strRole.equals(ERoles.ROLE_ADMIN.name())) {
                newRolesList.add(ERoles.ROLE_ADMIN);
            } else if (strRole.equals(ERoles.ROLE_STUDENT.name())) {
                newRolesList.add(ERoles.ROLE_STUDENT);
            }
        }
        List<Roles> newRoles = roleRepository.findByRoleNameIn(newRolesList);
        user.setRolesList(newRoles);
        userRepository.save(user);
        return userDTO;
    }

    @Override
    public void setStatus(Long id, Integer status) {
        if (userRepository.findById(id).isPresent()) {
            Users user = userRepository.findById(id).get();
            user.setStatus(status);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listALl() {
        List<Users> usersList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            List<String> roles = new ArrayList<>();
            List<Roles> rolesList = user.getRolesList();
            for (Roles role : rolesList) {
                roles.add(role.getRoleName().toString());
            }
            List<String> courses = new ArrayList<>();
            List<Courses> coursesList = user.getCoursesList();
            for (Courses course : coursesList) {
                roles.add(course.getCourseName());
            }
            user.setRolesList(null);
            user.setCoursesList(null);
            UserDTO userDTO = objectMapper.convertValue(user, UserDTO.class);
            userDTO.setRoleList(roles);
            userDTO.setCoursesList(courses);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listUserByStatus(Integer status) {
        List<Users> usersList = userRepository.findAllByStatus(status);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            List<String> roles = new ArrayList<>();
            List<Roles> rolesList = user.getRolesList();
            for (Roles role : rolesList) {
                roles.add(role.getRoleName().toString());
            }
            user.setRolesList(null);
            UserDTO userDTO = objectMapper.convertValue(user, UserDTO.class);
            userDTO.setRoleList(roles);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

}

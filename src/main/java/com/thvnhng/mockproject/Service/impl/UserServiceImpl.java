package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.*;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public int getTotalItem() {
        return (int) userRepository.count();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        Users user = objectMapper.convertValue(signUpRequest, Users.class);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        List<String> strRoles = signUpRequest.getRoleList();
        List<ERoles> eRolesList = new ArrayList<>();
        validUserRoleList(strRoles, eRolesList);
        List<Roles> roles = roleRepository.findByRoleNameIn(eRolesList);
        user.setRolesList(roles);
        userRepository.save(user);
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
    public void updateUserRoles(UserDTO userDTO) {
        Users user = userRepository.findByUsername(userDTO.getUsername());
        List<String> strNewRoles = userDTO.getRoleList();
        List<ERoles> newRolesList = new ArrayList<>();
        validUserRoleList(strNewRoles, newRolesList);
        List<Roles> newRoles = roleRepository.findByRoleNameIn(newRolesList);
        user.setRolesList(newRoles);
        userRepository.save(user);
    }

    @Override
    public void setDelete(Long id, String deletedBy, LocalDateTime deletedAt) {
        if (userRepository.findById(id).isPresent()) {
            Users user = userRepository.findById(id).get();
            user.setDeletedBy(deletedBy);
            user.setDeletedAt(deletedAt);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listALl() {
        List<Users> usersList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        convertLists(usersList, userDTOList);
        return userDTOList;
    }

    @Override
    public List<UserDTO> listALl(Pageable pageable) {
        List<Users> usersList = userRepository.findAllByDeletedAtIsNull(pageable);
        List<UserDTO> userDTOList = new ArrayList<>();
        convertLists(usersList, userDTOList);
        return userDTOList;
    }

    @Override
    public List<UserDTO> listUserDeleted() {
        List<Users> usersList = userRepository.findAllByDeletedAtIsNotNull();
        List<UserDTO> userDTOList = new ArrayList<>();
        convertLists(usersList, userDTOList);
        return userDTOList;
    }

    @Override
    public void validUserRoleList(List<String> strRoles, List<ERoles> eRolesList) {
        for (String strRole : strRoles) {
            if (strRole.equals(ERoles.ROLE_ADMIN.name())) {
                eRolesList.add(ERoles.ROLE_ADMIN);
            } else if (strRole.equals(ERoles.ROLE_STUDENT.name())) {
                eRolesList.add(ERoles.ROLE_STUDENT);
            }
        }
    }

    @Override
    public void convertLists(List<Users> usersList, List<UserDTO> userDTOList) {
        for (Users user : usersList) {
            List<String> roles = new ArrayList<>();
            List<String> courses = new ArrayList<>();
            List<String> reports = new ArrayList<>();
            List<Roles> rolesList = user.getRolesList();
            List<Courses> coursesList = user.getCoursesList();
            List<Reports> reportsList = user.getReportsList();
            for (Roles role : rolesList) {
                roles.add(role.getRoleName().toString());
            }
            for (Courses course : coursesList) {
                courses.add(course.getCourseName());
            }
            for (Reports report : reportsList) {
                reports.add(report.getReportName());
            }
            user.setRolesList(null);
            user.setCoursesList(null);
            user.setReportsList(null);
            UserDTO userDTO = objectMapper.convertValue(user, UserDTO.class);
            userDTO.setRoleList(roles);
            userDTO.setCoursesList(courses);
            userDTO.setReportsList(reports);
            userDTO.setPassword(null);
            userDTOList.add(userDTO);
        }
    }

}

package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.UserDTO;

import java.util.List;

public interface UserService {

    Boolean checkExistUsernameAndEmail(String username, String email);
    Boolean checkExistId(Long id);
    Boolean checkExistUsername(String username);
    Boolean checkExistEmail(String email);
    UserDTO detail(Long id);
    UserDTO updateUserInfo(UserDTO userDTO);
    UserDTO updateUserRoles(UserDTO userDTO);
    UserDTO setStatus(Long id, Integer status);
    List<UserDTO> listALl();
    List<UserDTO> listUserByStatus(Integer status);

}

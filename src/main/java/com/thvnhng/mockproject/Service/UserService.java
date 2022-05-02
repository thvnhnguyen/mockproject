package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    Boolean checkExistUsernameAndEmail(String username, String email);
    Boolean checkExistId(Long id);
    Boolean checkExistUsername(String username);
    Boolean checkExistEmail(String email);
    int getTotalItem();
    void signUp(SignUpRequest signUpRequest);
    UserDTO detail(Long id);
    UserDTO updateUserInfo(UserDTO userDTO);
    void updateUserRoles(UserDTO userDTO);
    void setDelete(Long id,String deletedBy, LocalDateTime deletedAt);
    List<UserDTO> listALl();
    List<UserDTO> listALl(Pageable pageable);
    List<UserDTO> listUserDeleted();
    void validUserRoleList(List<String> strRoles, List<ERoles> eRolesList);
    void convertLists(List<Users> usersList, List<UserDTO> userDTOList);

}

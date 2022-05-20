package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.ProfileDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    Boolean checkExistUsernameAndEmail(String username, String email);
    Boolean checkExistId(Long id);
    Boolean checkExistUsername(String username);
    Boolean checkExistEmail(String email);
    Boolean isStudentIsFree(Long userId);
    int getTotalItem();
    void signUp(SignUpRequest signUpRequest);
    UserDTO detailById(Long id);
    UserDTO detailByUsername(String username);
    ProfileDTO profile(String username);
    void updateProfile(ProfileDTO profileDTO);
    void updateUserRoles(UserDTO userDTO);
    void setDelete(Long id,String deletedBy, LocalDateTime deletedAt);
    List<UserDTO> listALl();
    List<UserDTO> listALl(Pageable pageable);
    List<UserDTO> listUserDeleted();
    String getEncodedPassword(String username);
    void validUserRoleList(List<String> strRoles, List<ERoles> eRolesList);
    void changePassword(String username, String newPassword);
    UserDTO convertListsToDTO(Users user);
    void generateOneTimePassword(Users user) throws UnsupportedEncodingException, MessagingException;
    void sendOTPEmail(Users user, String OTP) throws UnsupportedEncodingException, MessagingException;
    void clearOTP(Users user);

}

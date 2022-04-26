package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.ExcelModel.BaseExportExcelModel;
import com.thvnhng.mockproject.ExcelModel.UserExportExcelModel;
import com.thvnhng.mockproject.Exception.NotFoundExcep;
import com.thvnhng.mockproject.Service.ExportExcelFileService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.constant.SystemConstant;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "userAPI")
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final ExportExcelFileService exportExcelFileService;

    @GetMapping("/all")
    public List<UserDTO> list() {
        return userService.listALl();
    }

    @GetMapping("")
    public List<UserDTO> listByStatus(@RequestParam(name = "status") Integer status) {
        try{
            return userService.listUserByStatus(status);
        } catch (Exception ex) {
            throw new NotFoundExcep("Not found status = " + status, ex);
        }
    }

    @GetMapping("/{id}")
    public UserDTO detail(@PathVariable(name = "id") Long id) {
        try{
            return userService.detail(id);
        } catch (Exception ex) {
            throw new NotFoundExcep("Not found user with id = " + id, ex);
        }
    }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        if (userService.updateUserInfo(userDTO) == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Update profile failed"));
        }
        userService.updateUserInfo(userDTO);
        return ResponseEntity.ok().body(new MessageResponse("Updated"));
    }

    @PutMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        if (!userService.checkExistUsernameAndEmail(userDTO.getUsername(), userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username and email not valid"));
        }
        userService.updateUserInfo(userDTO);
        userService.updateUserRoles(userDTO);
        userService.setStatus(userDTO.getId(), userDTO.getStatus());
        return ResponseEntity.ok().body("Updated");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> active(@PathVariable(name = "id") Long id) {
        if (!userService.checkExistId(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("No user be found with id = " + id));
        }
        userService.setStatus(id, SystemConstant.ACTIVE_STATUS);
        return ResponseEntity.ok().body(new MessageResponse("User activated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> inactive(@PathVariable(name = "id") Long id) {
        if (!userService.checkExistId(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("No user be found with id = " + id));
        }
        userService.setStatus(id, SystemConstant.INACTIVE_STATUS);
        return ResponseEntity.ok().body(new MessageResponse("User inactivated"));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> export() {
        List<BaseExportExcelModel> metaList = new ArrayList<>();
        List<UserDTO> userDTOList = userService.listALl();
        for (UserDTO userDTO : userDTOList) {
            metaList.add(new UserExportExcelModel(
                    userDTO.getUsername(),
                    userDTO.getPassword(),
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getGender(),
                    userDTO.getContactNumber(),
                    userDTO.getAddress(),
                    userDTO.getBirthDate(),
                    userDTO.getStatus()));
        }
        exportExcelFileService.exportFile("DSUser", "userList", metaList, UserExportExcelModel.class);
        return ResponseEntity.ok().body(new MessageResponse("Success"));
    }

}

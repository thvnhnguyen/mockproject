package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.ExcelModel.BaseExportExcelModel;
import com.thvnhng.mockproject.ExcelModel.UserExportExcelModel;
import com.thvnhng.mockproject.Exception.NotFoundExcep;
import com.thvnhng.mockproject.Service.ExportExcelFileService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController(value = "userAPI")
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final ExportExcelFileService exportExcelFileService;

    @GetMapping("/all")
    public List<UserDTO> listAll(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        if (size > 100) {
            size = 100;
        }
        int totalPage = (int) Math.ceil((double) userService.getTotalItem() / size);
        if (page > totalPage) {
            throw new NotFoundExcep("Number of page not exist", new Throwable());
        }
        if ("asc".equals(order)) {
            Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            return userService.listALl(pageWithLimitAndSort);
        } else {
            if ("desc".equals(order)) {
                Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).descending());
                return userService.listALl(pageWithLimitAndSort);
            }
        }
        return null;
    }

    @GetMapping("/bin")
    public List<UserDTO> listDeleted() {
        try{
            return userService.listUserDeleted();
        } catch (Exception ex) {
            throw new NotFoundExcep("Not found user deleted ", ex);
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
        userService.setDelete(userDTO.getId(), userDTO.getDeletedBy(), userDTO.getDeletedAt());
        return ResponseEntity.ok().body("Updated");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> active(@PathVariable(name = "id") Long id) {
        if (!userService.checkExistId(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("No user be found with id = " + id));
        }
        userService.setDelete(id, null, null);
        return ResponseEntity.ok().body(new MessageResponse("User activated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> inactive(@PathVariable(name = "id") Long id) {
        if (!userService.checkExistId(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("No user be found with id = " + id));
        }
        userService.setDelete(id, "No Name", LocalDateTime.now());
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
                    userDTO.getDeletedAt()));
        }
        exportExcelFileService.exportFile("DSUser", "userList", metaList, UserExportExcelModel.class);
        return ResponseEntity.ok().body(new MessageResponse("Success"));
    }

}

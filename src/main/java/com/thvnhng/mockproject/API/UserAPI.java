package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.ExcelModel.BaseExportExcelModel;
import com.thvnhng.mockproject.ExcelModel.UserExportExcelModel;
import com.thvnhng.mockproject.Service.ExportExcelFileService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController(value = "userAPI")
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final ExportExcelFileService exportExcelFileService;

    //    List all active user with sort and paging
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listAll(
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
            return ResponseEntity.badRequest().body(new MessageResponse("Number of page not exist"));
        }
        Pageable pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        if ("desc".equals(order)) {
            pageWithLimitAndSort = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        List<UserDTO> listUser = userService.listALl(pageWithLimitAndSort);
        Map<String, Object> response = new HashMap<>();
        response.put("result", listUser);
        response.put("currentPage", page);
        response.put("totalItems", size);
        response.put("totalPages", totalPage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/bin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> listDeleted() {
        try {
            return ResponseEntity.ok().body(userService.listUserDeleted());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Not found");
        }
    }

    //    Profile page (has any role)


//    Detail page ()
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAIN_TEACHER','ROLE_SUBJECT_TEACHER')")
//    public ResponseEntity<?> detail(@PathVariable(name = "id") Long id) {
//        return null;
//    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        if (!userService.checkExistUsernameAndEmail(userDTO.getUsername(), userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username and email not valid"));
        }
//        userService.updateUserInfo(userDTO);
        userService.updateUserRoles(userDTO);
        userService.setDelete(userDTO.getId(), userDTO.getDeletedBy(), userDTO.getDeletedAt());
        return ResponseEntity.ok().body(new MessageResponse("Updated User !"));
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
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.setDelete(id, deletedBy, LocalDateTime.now());
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

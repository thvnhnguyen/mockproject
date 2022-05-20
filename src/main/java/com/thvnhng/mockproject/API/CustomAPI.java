package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.PasswordDTO;
import com.thvnhng.mockproject.DTO.ProfileDTO;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController(value = "customAPI")
@RequestMapping("")
@AllArgsConstructor
public class CustomAPI {

    private final UserService userService;
    private final PasswordEncoder encoder;
//Test ok
    @PutMapping("/accounts/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> editProfile(@Valid @RequestBody ProfileDTO profileDTO) {
        String permitUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        profileDTO.setUsername(permitUsername);
        userService.updateProfile(profileDTO);
        return ResponseEntity.ok().body(new MessageResponse("Edited Profile"));
    }

//    Test ok
    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> profile(@PathVariable(name = "username") String username) {
        String checkUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (checkUsername.equals(username)) {
            return ResponseEntity.ok().body(userService.profile(username));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("404 Not Found"));
    }

//    Test ok, optimize needed
    @PutMapping("/password/change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO) {
        String usernameChangePassword = SecurityContextHolder.getContext().getAuthentication().getName();
        String encodedCurrentPassword = userService.getEncodedPassword(usernameChangePassword);
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Please make sure both passwords match."));
        }
        if (passwordDTO.getCurrentPassword().equals(passwordDTO.getNewPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("The new password is the same as the current password"));
        }
        if (!encoder.matches(passwordDTO.getCurrentPassword(), encodedCurrentPassword)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Current password is not valid"));
        }
        userService.changePassword(usernameChangePassword, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new MessageResponse("Password update successful"));
    }

}

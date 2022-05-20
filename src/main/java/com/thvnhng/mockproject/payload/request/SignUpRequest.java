package com.thvnhng.mockproject.payload.request;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import com.thvnhng.mockproject.Valid.Annotation.CheckUser;
import com.thvnhng.mockproject.Valid.Annotation.ExistRoleName;
import com.thvnhng.mockproject.Valid.RegexString;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {

    @CheckUser
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 25, message = "Username contains between 6 and 25 characters")
    @Pattern(
            regexp = RegexString.USERNAME_PATTERN,
            message = "Username is not valid," +
                    " username contains only letters, numbers and '_' ," +
                    " username must start with a letter and be between 7 and 30 characters long")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 25, message = "Password contains between 6 and 25 characters")
    private String password;
    @Email(message = "Email is not valid (Valid Ex : username@domain.com)", regexp = RegexString.EMAIL_PATTERN)
    @NotBlank(message = "Email cannot be blank")
    @Size(min = 11, max = 100, message = "Email contains between 11 and 100 characters")
    @CheckEmail
    private String email;

//    @NotNull(message = "New user is registered with 1 and only 1 role")
    @NotEmpty(message = "Role cannot be empty, New user is registered with 1 and only 1 role")
    @NotBlank(message = "Role cannot be blank")
    @ExistRoleName
    private String roleUser;
    private String subjectName;
}

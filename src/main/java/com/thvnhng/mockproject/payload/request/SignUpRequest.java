package com.thvnhng.mockproject.payload.request;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import com.thvnhng.mockproject.Valid.Annotation.CheckUser;
import com.thvnhng.mockproject.Valid.RegexString;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {

    @CheckUser
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 25, message = "Username contains between 6 and 25 characters")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 25, message = "Password contains between 6 and 25 characters")
    private String password;
    @Email(message = "Email is not valid (Valid Ex : username@domain.com)", regexp = RegexString.EMAIL_PATTERN)
    @NotBlank(message = "Email cannot be blank")
    @Size(min = 11, max = 100, message = "Username contains between 11 and 100 characters")
    private String email;
    private List<String> roleList;
    private String subjectName;
}

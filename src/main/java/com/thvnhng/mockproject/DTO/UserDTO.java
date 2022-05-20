package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import com.thvnhng.mockproject.Valid.RegexString;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    protected String deletedBy;
    protected LocalDateTime deletedAt;
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, max = 25, message = "Username contains between 6 and 25 characters")
    @Pattern(
            regexp = RegexString.USERNAME_PATTERN,
            message = "Username is not valid," +
            " username contains only letters, numbers and '_' ," +
            " username must start with a letter and be between 7 and 30 characters long")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 25, message = "Password contains between 6 and 25 characters")
//    private String password;
    @Transient
    public String getFullName() {
        return firstName +
                " " +
                lastName;
    }
    @NotNull(message = "First name cannot be null")
    @Pattern(regexp = RegexString.NAME_PATTERN)
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    @Pattern(regexp = RegexString.NAME_PATTERN)
    private String lastName;
    @Email(message = "Email is not valid (Valid Ex : username@domain.com)", regexp = RegexString.EMAIL_PATTERN)
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email can not be null")
    @Size(min = 11, max = 100, message = "Email contains between 11 and 100 characters")
    @CheckEmail
    private String email;
    private String gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private List<String> roleList;
    private String subjectName;
    private List<String> coursesList;
    private List<String> scoresList;
}

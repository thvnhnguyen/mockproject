package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import com.thvnhng.mockproject.Valid.RegexString;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 25, message = "Password contains between 6 and 25 characters")
    private String password;
    @Transient
    public String getFullName() {
        StringBuilder name = new StringBuilder();
        name.append(firstName);
        name.append(" ");
        name.append(lastName);
        return name.toString();
    }
    private String firstName;
    private String lastName;
    @Email(message = "Email is not valid (Valid Ex : username@domain.com)", regexp = RegexString.EMAIL_PATTERN)
    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 11, max = 100, message = "Email contains between 11 and 100 characters")
    private String email;
    private Character gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private List<String> roleList;
    private String subjectName;
    private List<String> coursesList;
    private List<String> scoresList;
}

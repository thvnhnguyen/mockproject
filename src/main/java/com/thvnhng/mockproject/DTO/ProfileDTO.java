package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Valid.Annotation.CheckEmail;
import com.thvnhng.mockproject.Valid.RegexString;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ProfileDTO {

    private Long id;
    private LocalDateTime createDate;
    private String username;

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
    private String email;
    private String gender;
    private String contactNumber;
    private String address;
    private Date birthDate;

}

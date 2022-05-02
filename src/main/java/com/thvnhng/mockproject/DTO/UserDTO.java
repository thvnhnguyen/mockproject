package com.thvnhng.mockproject.DTO;

import lombok.Data;

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
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Character gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private List<String> roleList;
    private List<String> coursesList;
    private List<String> reportsList;
}

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
    private String username;
    private String password;
    private String email;
    private Character gender;
    private String contactNumber;
    private String address;
    private Date birthDate;
    private Integer status;
    private List<String> roleDTOList;
    private List<CourseDTO> courseDTOList;
}

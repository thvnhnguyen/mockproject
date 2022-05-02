package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Entity.Reports;
import com.thvnhng.mockproject.Entity.Users;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    protected String deletedBy;
    protected LocalDateTime deletedAt;
    private String courseName;
    private Integer schoolYear;
    private Integer status;
    private List<String> usersList;
    private List<String> reportsList;

}

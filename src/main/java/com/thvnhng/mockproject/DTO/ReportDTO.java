package com.thvnhng.mockproject.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Double studentMark;
    private UserDTO user;
    private CourseDTO course;
    private SubjectDTO subject;

}

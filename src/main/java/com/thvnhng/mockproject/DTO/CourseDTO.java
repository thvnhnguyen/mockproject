package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Valid.Annotation.CheckCourseName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    @CheckCourseName
    @NotBlank(message = "Name of Course cannot be empty")
    private String courseName;
    @NotBlank(message = "School Year cannot be empty")
    private Integer schoolYear;
    private List<String> usersList;
    private List<String> scoresList;

}

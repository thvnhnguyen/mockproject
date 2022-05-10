package com.thvnhng.mockproject.DTO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SubjectDTO {

    private Long id;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    protected String deletedBy;
    protected LocalDateTime deletedAt;
    @NotEmpty(message = "Name of role cannot be empty")
    private String subjectName;
    private List<String> reportsList;

}

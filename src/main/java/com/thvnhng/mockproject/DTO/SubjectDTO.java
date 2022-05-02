package com.thvnhng.mockproject.DTO;

import com.thvnhng.mockproject.Entity.Reports;
import lombok.Data;

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
    private String subjectName;
    private List<String> reportsList;

}

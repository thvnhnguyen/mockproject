package com.thvnhng.mockproject.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long id;
    private String username;
    private String courseName;
    private String subjectName;
    private String reportName;
    private Integer mark15m;
    private Integer mark45m;
    private Integer markFinal;
    private Double markSummary;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    protected String deletedBy;
    protected LocalDateTime deletedAt;
}

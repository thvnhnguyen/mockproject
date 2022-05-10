package com.thvnhng.mockproject.DTO;

import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ScoreDTO {

    private Long id;
    private String username;
    private String courseName;
    private String subjectName;
    private String scoreName;
    @Size(min = 0, max = 10, message = "Score between 0 and 100")
    private Double score;
    private String scoreType;
    private String createBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private String deletedBy;
    private LocalDateTime deletedAt;
}

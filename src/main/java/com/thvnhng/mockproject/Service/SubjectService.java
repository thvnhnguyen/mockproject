package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.SubjectDTO;
import com.thvnhng.mockproject.Entity.Scores;

import java.time.LocalDateTime;
import java.util.List;

public interface SubjectService {

    List<SubjectDTO> listAll();
    SubjectDTO detail(Long id);
    SubjectDTO create(SubjectDTO subjectDTO);
    void update(SubjectDTO subjectDTO);
    void setDelete(Long id, String deletedBy, LocalDateTime deletedAt);
    List<Scores> convertReportList(List<String> strReportList);

}

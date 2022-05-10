package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.ScoreDTO;
import com.thvnhng.mockproject.Entity.Scores;

import java.time.LocalDateTime;
import java.util.List;

public interface ScoreService {

    List<ScoreDTO> listAll();
    List<ScoreDTO> listBy(String username, String course, String subject);
    ScoreDTO detail(Long id);
    ScoreDTO create(ScoreDTO scoreDTO);
    void update(ScoreDTO scoreDTO, String username);
    void setDelete(Long id, String deletedBy, LocalDateTime deletedAt);
    boolean validTeacher(ScoreDTO scoreDTO, String username);
    List<Scores> convertReportList(List<String> strReportList);

}

package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.Entity.Reports;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<ReportDTO> listAll();
    List<ReportDTO> listBy(String userId, String course, String subject);
    ReportDTO detail(Long id);
    ReportDTO create(ReportDTO reportDTO);
    void update(ReportDTO reportDTO, String username);
    void setDelete(Long id, String deletedBy, LocalDateTime deletedAt);
    boolean validTeacher(ReportDTO reportDTO, String username);
    List<Reports> convertReportList(List<String> strReportList);
    Double markHandle(Integer mark15m, Integer mark45m, Integer markFinal);

}

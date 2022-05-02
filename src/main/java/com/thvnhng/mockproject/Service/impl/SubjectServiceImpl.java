package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.SubjectDTO;
import com.thvnhng.mockproject.Entity.Reports;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Repository.ReportRepository;
import com.thvnhng.mockproject.Repository.SubjectRepository;
import com.thvnhng.mockproject.Service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService{

    private final SubjectRepository subjectRepository;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<SubjectDTO> listAll() {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        List<Subjects> subjectsList = subjectRepository.findAll();
        for (Subjects subject : subjectsList) {
            subjectDTOList.add(objectMapper.convertValue(subject, SubjectDTO.class));
        }
        return subjectDTOList;
    }

    @Override
    public SubjectDTO detail(Long id) {
        return objectMapper.convertValue(subjectRepository.findByIdAndDeletedAtIsNull(id), SubjectDTO.class);
    }

    @Override
    public SubjectDTO create(SubjectDTO subjectDTO) {
        subjectRepository.save(objectMapper.convertValue(subjectDTO, Subjects.class));
        return subjectDTO;
    }

    @Override
    public void update(SubjectDTO subjectDTO) {
//        List<Reports> reportsList = convertReportList(subjectDTO.getReportsList());
        subjectDTO.setReportsList(null);
        Subjects subject = objectMapper.convertValue(subjectDTO, Subjects.class);
//        subject.setReportsList(reportsList);
        subjectRepository.save(subject);
    }

    @Override
    public void setDelete(Long id, String deletedBy, LocalDateTime deletedAt) {
        if (subjectRepository.findById(id).isPresent()) {
            Subjects subject = subjectRepository.findById(id).get();
            subject.setDeletedBy(deletedBy);
            subject.setDeletedAt(deletedAt);
            subjectRepository.save(subject);
        }
    }

    @Override
    public List<Reports> convertReportList(List<String> strReportList) {
//        List<Reports> reportsList = re
        return null;
    }


}

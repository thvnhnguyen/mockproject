package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.SubjectDTO;
import com.thvnhng.mockproject.Entity.Scores;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Repository.ScoreRepository;
import com.thvnhng.mockproject.Repository.SubjectRepository;
import com.thvnhng.mockproject.Service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Boolean isExistSubjectName(String subjectName) {
        return subjectRepository.existsBySubjectName(subjectName);
    }

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
    public SubjectDTO detail(Long subjectId) {
        Optional<Subjects> subjectsOptional = subjectRepository.findByIdAndDeletedAtIsNull(subjectId);
        if (subjectsOptional.isPresent()) {
            Subjects subject = subjectsOptional.get();
            subject.setUsersList(null);
            subject.setScoresList(null);
            return objectMapper.convertValue(subject, SubjectDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public SubjectDTO create(SubjectDTO subjectDTO) {
        subjectRepository.save(objectMapper.convertValue(subjectDTO, Subjects.class));
        return subjectDTO;
    }

    @Override
    public void update(SubjectDTO subjectDTO) {
        Optional<Subjects> subjectsOptional = subjectRepository.findById(subjectDTO.getId());
        if (subjectsOptional.isPresent()) {
            Subjects subject = subjectsOptional.get();
            subject.setSubjectName(subjectDTO.getSubjectName());
            subjectRepository.save(subject);
        }
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
    public List<Scores> convertScoreList(List<String> strReportList) {
        return null;
    }


}

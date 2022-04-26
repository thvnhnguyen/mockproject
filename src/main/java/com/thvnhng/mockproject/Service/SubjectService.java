package com.thvnhng.mockproject.Service;

import com.thvnhng.mockproject.DTO.SubjectDTO;

import java.util.List;

public interface SubjectService {

    List<SubjectDTO> listAll();
    SubjectDTO detail(Long id);
    SubjectDTO create(SubjectDTO subjectDTO);
    SubjectDTO update(SubjectDTO subjectDTO);
    void delete(Long id);

}

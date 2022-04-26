package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.constant.SystemConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Boolean checkExistId(Long id) {
        return courseRepository.existsCoursesById(id);
    }

    @Override
    public List<CourseDTO> listAll() {
        List<Courses> coursesList = courseRepository.findAll();
        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Courses course : coursesList) {
            courseDTOList.add(objectMapper.convertValue(course, CourseDTO.class));
        }
        return courseDTOList;
    }

    @Override
    public CourseDTO detail(Long id) {
        return objectMapper.convertValue(courseRepository.findById(id), CourseDTO.class);
    }

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        Courses course = objectMapper.convertValue(courseDTO, Courses.class);
        course.setStatus(SystemConstant.ACTIVE_STATUS);
        course.setUsersList(userRepository.findAllByUsernameIn(courseDTO.getUsersList()));
        courseRepository.save(course);
        return courseDTO;
    }

    @Override
    @Transactional
    public CourseDTO update(CourseDTO courseDTO) {
        Optional<Courses> coursesOptional = courseRepository.findById(courseDTO.getId());
        if (coursesOptional.isPresent()) {
            Courses course = objectMapper.convertValue(courseDTO, Courses.class);
            course.setUsersList(userRepository.findAllByUsernameIn(courseDTO.getUsersList()));
            courseRepository.save(course);
            return courseDTO;
        }
        return null;
    }

    @Override
    public void setStatus(Long id, Integer status) {
        Optional<Courses> coursesOptional = courseRepository.findById(id);
        if (coursesOptional.isPresent()) {
            coursesOptional.get().setStatus(status);
            courseRepository.save(coursesOptional.get());
        }
    }

}

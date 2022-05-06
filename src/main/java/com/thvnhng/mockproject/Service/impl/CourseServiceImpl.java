package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.Entity.Reports;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Exception.NotFoundExcep;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
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
    public List<UserDTO> listStudent(Long id, Pageable pageable) {
        if (courseRepository.findById(id).isPresent()) {
            List<Users> usersList = userRepository.findAllByCoursesListContainingAndRolesListContaining(
                    courseRepository.getById(id),
                    roleRepository.findByRoleName(ERoles.ROLE_STUDENT),
                    pageable
            );
//            List<Users> usersList = courseRepository.findById(id).get().getUsersList();
            List<UserDTO> userDTOList = new ArrayList<>();
            userService.convertLists(usersList, userDTOList);
            return userDTOList;
        }
        return null;
    }

    @Override
    public CourseDTO detail(Long id) {
        if (courseRepository.findById(id).isPresent()) {
            Courses course = courseRepository.findById(id).get();
            List<String> strUserList = new ArrayList<>();
            List<String> strReportList = new ArrayList<>();
            convertToDTO(strUserList, course.getUsersList());
            convertToDTO(strReportList, course.getReportsList());
            course.setUsersList(null);
            course.setReportsList(null);
            CourseDTO courseDTO = objectMapper.convertValue(course, CourseDTO.class);
            courseDTO.setUsersList(strUserList);
            courseDTO.setReportsList(strReportList);
            return courseDTO;
        }
        return null;
    }

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        Courses course = objectMapper.convertValue(courseDTO, Courses.class);
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
    public void saveMainTeacher(Long id, UserDTO userDTO) {
        if (courseRepository.findById(id).isPresent()) {
            Users user = userRepository.getById(userDTO.getId());
            user.getRolesList().add(roleRepository.findByRoleName(ERoles.ROLE_MAIN_TEACHER));
            user = userRepository.save(user);
            Courses course = courseRepository.findById(id).get();
            course.getUsersList().add(user);
            courseRepository.save(course);
        }
    }

    @Override
    public void saveSubjectTeacher(Long courseId, Long userId) {
        if (courseRepository.findById(courseId).isPresent()) {
            Users user = userRepository.getById(userId);
            Courses course = courseRepository.getById(courseId);
            course.getUsersList().add(user);
            courseRepository.save(course);
        }
    }

    @Override
    public void setDelete(Long id,String deletedBy, LocalDateTime deletedAt) {
        Optional<Courses> coursesOptional = courseRepository.findById(id);
        if (coursesOptional.isPresent()) {
            coursesOptional.get().setDeletedBy(deletedBy);
            coursesOptional.get().setDeletedAt(deletedAt);
            courseRepository.save(coursesOptional.get());
        }
    }

    @Override
    public void convertToDTO(List<String> stringList, List<?> entityList) {
        for (Object obj : entityList) {
            if (obj instanceof Users) {
                stringList.add(((Users) obj).getUsername());
            } else if (obj instanceof Reports) {
                stringList.add(((Reports) obj).getReportName());
            }
        }
    }

}

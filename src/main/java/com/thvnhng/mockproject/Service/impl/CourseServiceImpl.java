package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.*;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.CourseService;
import com.thvnhng.mockproject.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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

//    ok
    @Override
    public Boolean checkExistMainTeacher(String courseName, Sort sort) {
        Courses course = courseRepository.findByCourseName(courseName);
        List<Users> mainTeacherList = userRepository.findAllByCoursesListContainingAndRolesListContaining(
                course,
                roleRepository.findByRoleName(ERoles.ROLE_MAIN_TEACHER),
                sort
        );
        if (mainTeacherList.size() > 0) {
            for (Users teacher : mainTeacherList) {
                if (teacher.getCourseNamePermit().equals(course.getCourseName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean checkExistSubjectTeacher(String courseName, Long userId) {
        List<Users> usersList =  userRepository.findAllBySubjectUser(userRepository.getById(userId).getSubjectUser());
        for (Users user : usersList) {
            if (user.getCoursesList().contains(courseRepository.findByCourseName(courseName))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isExistCourse(String courseName) {
        return courseRepository.existsCoursesByCourseName(courseName);
    }

    @Override
    public List<CourseDTO> listAll(Sort sort) {
        List<Courses> coursesList = courseRepository.findAll(sort);
        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Courses course : coursesList) {
            courseDTOList.add(convertToDTO(course));
        }
        return courseDTOList;
    }

    @Override
    public List<UserDTO> listStudent(Long id, Sort sort) {
        if (courseRepository.findById(id).isPresent()) {
            List<Users> usersList = userRepository.findAllByCoursesListContainingAndRolesListContaining(
                    courseRepository.getById(id),
                    roleRepository.findByRoleName(ERoles.ROLE_STUDENT),
                    sort
            );
            return listUserToDTO(usersList);
        }
        return null;
    }

    @Override
    public List<UserDTO> listTeacher(Long id, Sort sort) {
        if (courseRepository.findById(id).isPresent()) {
            List<Users> teacherList = userRepository.findAllByCoursesListContainingAndRolesListContaining(
                    courseRepository.getById(id),
                    roleRepository.findByRoleName(ERoles.ROLE_SUBJECT_TEACHER),
                    sort
            );
            return listUserToDTO(teacherList);
        }
        return null;
    }

    public List<UserDTO> listUserToDTO(List<Users> usersList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(userService.convertListsToDTO(user));
        }
        return userDTOList;
    }

    @Override
    public CourseDTO detail(Long id) {
        return convertToDTO(courseRepository.getById(id));
    }

    @Override
    public CourseDTO detailByCourseName(String courseName) {
        return convertToDTO(courseRepository.findByCourseName(courseName));
    }

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        Courses course = objectMapper.convertValue(courseDTO, Courses.class);
//        course.setUsersList(userRepository.findAllByUsernameIn(courseDTO.getUsersList()));
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
    public void saveMainTeacher(String courseName, UserDTO userDTO) {
        String username = userDTO.getUsername();
        Users user = userRepository.findUsersByUsername(username);
//        List<ERoles> eRolesList = new ArrayList<>();
//        eRolesList.add(ERoles.ROLE_SUBJECT_TEACHER);
//        eRolesList.add(ERoles.ROLE_MAIN_TEACHER);
//        List<Roles> rolesList = roleRepository.findByRoleNameIn(eRolesList);
//        user.setRolesList(rolesList);
        user.getRolesList().add(roleRepository.findByRoleName(ERoles.ROLE_MAIN_TEACHER));
        user.setCourseNamePermit(courseName);
        Courses course = courseRepository.findByCourseName(courseName);
        course.getUsersList().add(user);
//        courseRepository.save(course);
    }

    @Override
    public void saveSubjectTeacher(String courseName, Long userId) {
        Users user = userRepository.getById(userId);
        Courses course = courseRepository.findByCourseName(courseName);
        course.getUsersList().add(user);
        courseRepository.save(course);
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
    public CourseDTO convertToDTO(Courses course) {
        List<String> strUserList = new ArrayList<>();
        List<String> strScoreList = new ArrayList<>();
        List<Users> usersList = course.getUsersList();
        List<Scores> scoresList = course.getScoresList();
        for (Users user : usersList) {
            strUserList.add(user.getFullName());
        }
        for (Scores score : scoresList) {
            strScoreList.add(score.getScoreToList());
        }
        course.setUsersList(null);
        course.setScoresList(null);
        CourseDTO courseDTO = objectMapper.convertValue(course, CourseDTO.class);
        courseDTO.setUsersList(strUserList);
        courseDTO.setScoresList(strScoreList);
        return courseDTO;
    }

}

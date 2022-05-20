package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.CourseDTO;
import com.thvnhng.mockproject.DTO.CourseResponseDTO;
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
        return courseRepository.existsCoursesByIdAndDeletedAtIsNull(id);
    }

//    ok
    @Override
    public Boolean checkExistMainTeacher(Long courseId, Sort sort) {
        Courses course = courseRepository.getById(courseId);
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
    public List<CourseResponseDTO> listAll(Sort sort) {
        List<Courses> coursesList = courseRepository.findAll(sort);
        List<CourseResponseDTO> courseDTOList = new ArrayList<>();
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
    public CourseResponseDTO detail(Long id) {
        Optional<Courses> coursesOptional = courseRepository.findById(id);
        return convertToDTO(coursesOptional.get());
    }

    @Override
    public CourseResponseDTO detailByCourseName(String courseName) {
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
    public void saveMainTeacher(Long courseId, UserDTO userDTO) {
        Long id = 18L;
        Users user = userRepository.findById(id).get();
//        List<ERoles> eRolesList = new ArrayList<>();
//        eRolesList.add(ERoles.ROLE_SUBJECT_TEACHER);
//        eRolesList.add(ERoles.ROLE_MAIN_TEACHER);
//        List<Roles> rolesList = roleRepository.findByRoleNameIn(eRolesList);
//        user.setRolesList(rolesList);
//        user.getRolesList().add(roleRepository.findByRoleName(ERoles.ROLE_MAIN_TEACHER));
        Courses course = courseRepository.findById(courseId).get();
        user.setCourseNamePermit(course.getCourseName());
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
    public boolean saveStudentToCourse(Long courseId, List<Long> userIds) {
        for (Long userId : userIds) {
            if (!userService.isStudentIsFree(userId)) {
                return false;
            }
            Users user = userRepository.getById(userId);
            Courses course = courseRepository.getById(courseId);
            course.getUsersList().add(user);
            courseRepository.save(course);
        }
        return true;
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
    public CourseResponseDTO convertToDTO(Courses course) {
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
        CourseResponseDTO courseResponseDTO = objectMapper.convertValue(course, CourseResponseDTO.class);
        courseResponseDTO.setUsersList(strUserList);
        courseResponseDTO.setScoresList(strScoreList);
        return courseResponseDTO;
    }

    @Override
    public boolean checkTeacherPermission(String username, Long courseId) {
        Users user = userRepository.findAllByUsernameAndDeletedAtIsNull(username);
        Roles role = roleRepository.findByRoleName(ERoles.ROLE_ADMIN);
        if (user.getRolesList().contains(role)) {
            return true;
        }
        List<Courses> coursesList = user.getCoursesList();
        Optional<Courses> courses = courseRepository.findById(courseId);
        return coursesList.contains(courses.get());
    }

}

package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.*;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.RoleRepository;
import com.thvnhng.mockproject.Repository.SubjectRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.request.SignUpRequest;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder encoder;

    @Override
    public Boolean checkExistUsernameAndEmail(String username, String email) {
        return userRepository.existsByUsernameAndEmail(username, email);
    }

    @Override
    public Boolean checkExistId(Long id) {
        return userRepository.existsUsersById(id);
    }

    @Override
    public Boolean checkExistUsername(String username) {
        return userRepository.existsUsersByUsername(username);
    }

    @Override
    public Boolean checkExistEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Override
    public int getTotalItem() {
        return (int) userRepository.count();
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        Users user = objectMapper.convertValue(signUpRequest, Users.class);
        if (signUpRequest.getSubjectName() != null) {
            Subjects teacherSubject = subjectRepository.findBySubjectName(signUpRequest.getSubjectName());
            user.setSubjectUser(teacherSubject);
        }
        user.setFirstName("user");
        user.setLastName(RandomString.make(10));
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        List<String> strRoles = signUpRequest.getRoleList();
        List<ERoles> eRolesList = new ArrayList<>();
        validUserRoleList(strRoles, eRolesList);
        List<Roles> roles = roleRepository.findByRoleNameIn(eRolesList);
        user.setRolesList(roles);
        userRepository.save(user);
    }

    @Override
    public UserDTO detailById(Long id) {
        Users user = userRepository.getById(id);
        return convertListsToDTO(user);
    }

    @Override
    public UserDTO detailByUsername(String username) {
        Users user = userRepository.findUsersByUsername(username);
        return convertListsToDTO(user);
    }

    @Override
    public Users detail(String username) {
        return userRepository.findUsersByUsername(username);
    }

    @Override
    public UserDTO updateUserInfo(UserDTO userDTO) {
        Users user = userRepository.findUsersByUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setContactNumber(userDTO.getContactNumber());
        user.setAddress(userDTO.getAddress());
        user.setBirthDate(userDTO.getBirthDate());
        userRepository.save(user);
        return userDTO;
    }

    @Override
    public void updateUserRoles(UserDTO userDTO) {
        Users user = userRepository.findUsersByUsername(userDTO.getUsername());
        List<String> strNewRoles = userDTO.getRoleList();
        List<ERoles> newRolesList = new ArrayList<>();
        validUserRoleList(strNewRoles, newRolesList);
        List<Roles> newRoles = roleRepository.findByRoleNameIn(newRolesList);
        user.setRolesList(newRoles);
        userRepository.save(user);
    }

    @Override
    public void setDelete(Long id, String deletedBy, LocalDateTime deletedAt) {
        if (userRepository.findById(id).isPresent()) {
            Users user = userRepository.findById(id).get();
            user.setDeletedBy(deletedBy);
            user.setDeletedAt(deletedAt);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listALl() {
        List<Users> usersList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(convertListsToDTO(user));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listALl(Pageable pageable) {
        List<Users> usersList = userRepository.findAllByDeletedAtIsNull(pageable);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(convertListsToDTO(user));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listUserDeleted() {
        List<Users> usersList = userRepository.findAllByDeletedAtIsNotNull();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : usersList) {
            userDTOList.add(convertListsToDTO(user));
        }
        return userDTOList;
    }

    @Override
    public String getEncodedPassword(String username) {
        return userRepository.findUsersByUsername(username).getPassword();
    }

    @Override
    public void validUserRoleList(List<String> strRoles, List<ERoles> eRolesList) {
        for (String strRole : strRoles) {
            if (strRole.equals(ERoles.ROLE_ADMIN.name())) {
                eRolesList.add(ERoles.ROLE_ADMIN);
            } else if (strRole.equals(ERoles.ROLE_STUDENT.name())) {
                eRolesList.add(ERoles.ROLE_STUDENT);
            } else if (strRole.equals(ERoles.ROLE_MAIN_TEACHER.name())) {
                eRolesList.add(ERoles.ROLE_MAIN_TEACHER);
            } else if (strRole.equals(ERoles.ROLE_SUBJECT_TEACHER.name())) {
                eRolesList.add(ERoles.ROLE_SUBJECT_TEACHER);
            }
        }
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Users user = userRepository.findUsersByUsername(username);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDTO convertListsToDTO(Users user) {
        List<String> roles = new ArrayList<>();
        List<String> courses = new ArrayList<>();
        List<String> scores = new ArrayList<>();
        List<Roles> rolesList = user.getRolesList();
        List<Courses> coursesList = user.getCoursesList();
        List<Scores> scoresList = user.getScoresList();
        for (Roles role : rolesList) {
            roles.add(role.getRoleName().toString());
        }
        for (Courses course : coursesList) {
            courses.add(course.getCourseName());
        }
        for (Scores score : scoresList) {
            scores.add(score.getScoreName());
        }
        user.setRolesList(null);
        user.setCoursesList(null);
        user.setScoresList(null);
        user.setSubjectUser(null);
        user.setPassword(null);
        UserDTO userDTO = objectMapper.convertValue(user, UserDTO.class);
        if (user.getSubjectUser() != null) {
            String subjectName = user.getSubjectUser().getSubjectName();
            userDTO.setSubjectName(subjectName);
        }
        userDTO.setRoleList(roles);
        userDTO.setCoursesList(courses);
        userDTO.setScoresList(scores);
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public void generateOneTimePassword(Users user) throws UnsupportedEncodingException, MessagingException {
        String OTP = RandomString.make(6);
        String encodedOTP = encoder.encode(OTP);
        user.setOneTimePassword(encodedOTP);
        user.setOtpRequestedTime(new Date());
        userRepository.save(user);
        sendOTPEmail(user, OTP);
    }

    @Override
    public void sendOTPEmail(Users user, String OTP) throws UnsupportedEncodingException, javax.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@thvnhng.com", "System Support");
        helper.setTo(user.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
        String content = "<p>Hello " + user.getFirstName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void clearOTP(Users user) {
        user.setOneTimePassword(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);
    }

    @Override
    public void saveMainTeacher(String courseName, UserDTO userDTO) {
        String username = userDTO.getUsername();
        Users user = detail(username);
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


}

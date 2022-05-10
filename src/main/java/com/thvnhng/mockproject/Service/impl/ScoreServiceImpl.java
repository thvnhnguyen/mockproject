package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.ScoreDTO;
import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.Scores;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.ScoreRepository;
import com.thvnhng.mockproject.Repository.SubjectRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.ScoreService;
import com.thvnhng.mockproject.constant.SystemConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    public List<ScoreDTO> listAll() {
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        List<Scores> scoresList = scoreRepository.findAllByDeletedAtIsNull();
        for (Scores score : scoresList) {
            scoreDTOList.add(objectMapper.convertValue(score, ScoreDTO.class));
        }
        return scoreDTOList;
    }

    @Override
    public List<ScoreDTO> listBy(String username, String course, String subject) {

        List<Scores> scoresListByUser = scoreRepository.findAllByUserIn(userRepository.findAllByUsernameContaining(username));
        List<Scores> scoresListByCourse = scoreRepository.findAllByCourseIn(courseRepository.findAllByCourseNameContaining(course));
        List<Scores> scoresListBySubject = scoreRepository.findAllBySubjectIn(subjectRepository.findAllBySubjectNameContaining(subject));
        if (scoresListBySubject == null || scoresListByCourse == null || scoresListByUser == null) {
            return null;
        }
        scoresListByUser.retainAll(scoresListByCourse);
        scoresListBySubject.retainAll(scoresListByUser);
        List<ScoreDTO> resultListBy = new ArrayList<>();
        for (Scores score : scoresListBySubject) {
            String user = score.getUser().getFullName();
            String courseName = score.getCourse().getCourseName();
            String subjectName = score.getSubject().getSubjectName();
            score.setUser(null);
            score.setCourse(null);
            score.setSubject(null);
            ScoreDTO scoreDTO = objectMapper.convertValue(score, ScoreDTO.class);
            scoreDTO.setUsername(user);
            scoreDTO.setCourseName(courseName);
            scoreDTO.setSubjectName(subjectName);
            resultListBy.add(scoreDTO);
        }
        return resultListBy;
    }

    public ScoreDTO detail(Long id) {
        return objectMapper.convertValue(scoreRepository.findById(id), ScoreDTO.class);
    }

    public ScoreDTO create(ScoreDTO scoreDTO) {
        String userFullName = userRepository.findUsersByUsername(scoreDTO.getUsername()).getFullName();
        Scores score = objectMapper.convertValue(scoreDTO, Scores.class);
        score.setUser(userRepository.findUsersByUsername(scoreDTO.getUsername()));
        score.setCourse(courseRepository.findByCourseName(scoreDTO.getCourseName()));
        score.setSubject(subjectRepository.findBySubjectName(scoreDTO.getSubjectName()));
        String scoreName = scoreDTO.getScoreType() + " " + scoreDTO.getSubjectName() + " of " + userFullName;
        score.setScoreName(scoreName);
        score.setScore(scoreDTO.getScore());
        score.setScoreType(scoreDTO.getScoreType());
        scoreRepository.save(score);
        return scoreDTO;
    }

    public void update(ScoreDTO scoreDTO, String username) {
        Scores score = scoreRepository.getById(scoreDTO.getId());
        score.setScore(scoreDTO.getScore());
        scoreRepository.save(score);
    }


    public void setDelete(Long id, String deletedBy, LocalDateTime deletedAt) {
    }

    //    Check teacher co dung lop va dung mon hay k
    @Override
    public boolean validTeacher(ScoreDTO scoreDTO, String username) {
        Users teacher = userRepository.findUsersByUsername(username);
        List<Courses> teacherCourse = teacher.getCoursesList();
        Courses course = courseRepository.findByCourseName(scoreDTO.getCourseName());
        Subjects scoreSubject = subjectRepository.findBySubjectName(scoreDTO.getSubjectName());
        Subjects teacherSubject = teacher.getSubjectUser();
        return teacherCourse.contains(course) && scoreSubject.equals(teacherSubject);
    }

    public List<Scores> convertReportList(List<String> strReportList) {
        return null;
    }

}

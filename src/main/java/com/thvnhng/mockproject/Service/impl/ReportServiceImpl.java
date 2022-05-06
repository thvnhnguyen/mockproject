package com.thvnhng.mockproject.Service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.Entity.Courses;
import com.thvnhng.mockproject.Entity.Reports;
import com.thvnhng.mockproject.Entity.Subjects;
import com.thvnhng.mockproject.Entity.Users;
import com.thvnhng.mockproject.Repository.CourseRepository;
import com.thvnhng.mockproject.Repository.ReportRepository;
import com.thvnhng.mockproject.Repository.SubjectRepository;
import com.thvnhng.mockproject.Repository.UserRepository;
import com.thvnhng.mockproject.Service.ReportService;
import com.thvnhng.mockproject.constant.SystemConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    public List<ReportDTO> listAll() {
        List<ReportDTO> reportDTOList = new ArrayList<>();
        List<Reports> reportsList = reportRepository.findAllByDeletedAtIsNull();
        for (Reports report : reportsList) {
            reportDTOList.add(objectMapper.convertValue(report, ReportDTO.class));
        }
        return reportDTOList;
    }

    @Override
    public List<ReportDTO> listBy(String username, String course, String subject) {

        List<Reports> reportsListByUser = reportRepository.findAllByUserIn(userRepository.findAllByUsernameContaining(username));
        List<Reports> reportsListByCourse = reportRepository.findAllByCourseIn(courseRepository.findAllByCourseNameContaining(course));
        List<Reports> reportsListBySubject = reportRepository.findAllBySubjectIn(subjectRepository.findAllBySubjectNameContaining(subject));
        if (reportsListBySubject == null || reportsListByCourse == null || reportsListByUser == null) {
            return null;
        }
        reportsListByUser.retainAll(reportsListByCourse);
        reportsListBySubject.retainAll(reportsListByUser);
        List<ReportDTO> resultListBy = new ArrayList<>();
        for (Reports report : reportsListBySubject) {
            String user = report.getUser().getUsername();
            String courseName = report.getCourse().getCourseName();
            String subjectName = report.getSubject().getSubjectName();
            report.setUser(null);
            report.setCourse(null);
            report.setSubject(null);
            ReportDTO reportDTO = objectMapper.convertValue(report, ReportDTO.class);
            reportDTO.setUsername(user);
            reportDTO.setCourseName(courseName);
            reportDTO.setSubjectName(subjectName);
            resultListBy.add(reportDTO);
        }
        return resultListBy;
    }

    public ReportDTO detail(Long id) {
        return objectMapper.convertValue(reportRepository.findById(id), ReportDTO.class);
    }

    public ReportDTO create(ReportDTO reportDTO) {
        String username = reportDTO.getUsername();
        String courseName = reportDTO.getCourseName();
        String subjectName = reportDTO.getSubjectName();
        Reports report = objectMapper.convertValue(reportDTO, Reports.class);
        report.setUser(userRepository.findByUsername(username));
        report.setCourse(courseRepository.findByCourseName(courseName));
        report.setSubject(subjectRepository.findBySubjectName(subjectName));
        report.setMarkSummary(markHandle(reportDTO.getMark15m(), reportDTO.getMark45m(), reportDTO.getMarkFinal()));
        reportRepository.save(report);
        return reportDTO;
    }

    public void update(ReportDTO reportDTO, String username) {
        Reports report = reportRepository.getById(reportDTO.getId());
        report.setMark15m(reportDTO.getMark15m());
        report.setMark45m(reportDTO.getMark45m());
        report.setMarkFinal(reportDTO.getMarkFinal());
        report.setMarkSummary(markHandle(reportDTO.getMark15m(), reportDTO.getMark45m(), reportDTO.getMarkFinal()));
        reportRepository.save(report);
    }


    public void setDelete(Long id, String deletedBy, LocalDateTime deletedAt) {
    }

    //    Check teacher co dung lop va dung mon hay k
    @Override
    public boolean validTeacher(ReportDTO reportDTO, String username) {
        Users teacher = userRepository.findByUsername(username);
        List<Courses> teacherCourse = teacher.getCoursesList();
        Reports report = reportRepository.getById(reportDTO.getId());
        Courses course = report.getCourse();
        Subjects studentSubject = report.getSubject();
        Subjects teacherSubject = teacher.getSubject_user();
        return teacherCourse.contains(course) && studentSubject.equals(teacherSubject);
    }

    public List<Reports> convertReportList(List<String> strReportList) {
        return null;
    }

    @Override
    public Double markHandle(Integer mark15m, Integer mark45m, Integer markFinal) {
        Double summary = Double.valueOf((mark15m * SystemConstant.MARK_15M_COEFFICIENT + mark45m * SystemConstant.MARK_45M_COEFFICIENT + markFinal * SystemConstant.MARK_FINAL_COEFFICIENT));
        //Multiply the result by 10 then round and divide by 10 to get a 1-digit decimal after the ","
        return Math.ceil(summary/SystemConstant.SUM_COEFFICIENT*10.0)/10.0;
    }


}

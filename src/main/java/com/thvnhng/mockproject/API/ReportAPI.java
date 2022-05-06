package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.ReportDTO;
import com.thvnhng.mockproject.Service.ReportService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportAPI {

    private final ReportService reportService;

//    List report diem co filter theo username, course, subject (chua valid)
    @GetMapping("/all")
    public List<ReportDTO> listBy(
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "course", required = false, defaultValue = "") String course,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
        return reportService.listBy(username, course, subject);
    }

//    Detail report diem
    @GetMapping("/{id}")
    public ReportDTO detail(@PathVariable(name = "id") Long id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReportDTO reportDTO) {
        reportService.create(reportDTO);
        return ResponseEntity.ok().body(new MessageResponse(
                "Created new report with username : " + reportDTO.getUsername() +
                ", course : " + reportDTO.getCourseName() +
                ", subject : " + reportDTO.getSubjectName()));
    }

//    Update diem report
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUBJECT_TEACHER')")
    public ResponseEntity<?> update(@RequestBody ReportDTO reportDTO) {
        String checkUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (reportService.validTeacher(reportDTO, checkUsername)) {
            reportService.update(reportDTO, checkUsername);
            return ResponseEntity.ok().body(new MessageResponse("Updated report!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized - You can't updated this report"));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete() {
        return null;
    }

}

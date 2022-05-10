package com.thvnhng.mockproject.API;

import com.thvnhng.mockproject.DTO.ScoreDTO;
import com.thvnhng.mockproject.DTO.UserDTO;
import com.thvnhng.mockproject.Entity.ERoles;
import com.thvnhng.mockproject.ExcelModel.BaseExportExcelModel;
import com.thvnhng.mockproject.ExcelModel.ReportExportExcelModel;
import com.thvnhng.mockproject.Service.ExportExcelFileService;
import com.thvnhng.mockproject.Service.ScoreService;
import com.thvnhng.mockproject.Service.UserService;
import com.thvnhng.mockproject.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/scores")
@AllArgsConstructor
public class ScoreAPI {

    private final ScoreService reportService;
    private final UserService userService;
    private final ExportExcelFileService exportExcelFileService;

    //    List report diem co filter theo username, course, subject (chua valid)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ScoreDTO> listBy(
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "course", required = false, defaultValue = "") String course,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
        return reportService.listBy(username, course, subject);
    }

    //    Detail report diem
    @GetMapping("/{id}")
    public ScoreDTO detail(@PathVariable(name = "id") Long id) {
        return null;
    }

    //    tAO DIEM
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUBJECT_TEACHER')")
    public ResponseEntity<?> create(@RequestBody ScoreDTO scoreDTO) {
        String checkUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (reportService.validTeacher(scoreDTO, checkUsername)) {
            reportService.create(scoreDTO);
            return ResponseEntity.ok().body(new MessageResponse(
                    "Created new SCORE of username : " + scoreDTO.getUsername() +
                            ", course : " + scoreDTO.getCourseName() +
                            ", subject : " + scoreDTO.getSubjectName()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized - You can't updated this report"));
        }
    }

    //    Update diem report
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUBJECT_TEACHER')")
    public ResponseEntity<?> update(@RequestBody ScoreDTO scoreDTO) {
        String checkUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (reportService.validTeacher(scoreDTO, checkUsername)) {
            reportService.update(scoreDTO, checkUsername);
            return ResponseEntity.ok().body(new MessageResponse("Updated report!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Unauthorized - You can't updated this report"));
        }
    }

    @GetMapping(value = {"/export"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAIN_TEACHER','ROLE_SUBJECT_TEACHER')")
    public ResponseEntity<?> export(
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "course", required = false, defaultValue = "") String course,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject
    ) {
//        List<Roles> rolesList = SecurityContextHolder.getContext().getAuthentication().get
        List<BaseExportExcelModel> metaList = new ArrayList<>();
        List<ScoreDTO> scoreDTOList = reportService.listBy(username, course, subject);
        for (ScoreDTO scoreDTO : scoreDTOList) {
            metaList.add(new ReportExportExcelModel(
                    scoreDTO.getUsername(),
                    scoreDTO.getCourseName(),
                    scoreDTO.getSubjectName(),
                    scoreDTO.getScoreName(),
                    scoreDTO.getScore(),
                    scoreDTO.getScoreType()));
        }
        String sheetName = "Scores of " + username + "_" + course + "_" + subject;
        exportExcelFileService.exportFile("ListScore", sheetName, metaList, ReportExportExcelModel.class);
        return ResponseEntity.ok().body(new MessageResponse("Download file success"));
    }

    @DeleteMapping
    public ResponseEntity<?> delete() {
        return null;
    }

    public ResponseEntity<?> checkPermissionScore(String username, String course, String subject) {
        if (!userService.checkExistUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("404 Not Found"));
        }
        if (userService.checkExistUsername(username)) {
            if (!userService.detailByUsername(username).getRoleList().contains(ERoles.ROLE_STUDENT.name())) {
                return ResponseEntity.badRequest().body(new MessageResponse("No student found with username = " + username));
            }
        }
        UserDTO student = userService.detailByUsername(username);
        String checkUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userPermit = userService.detailByUsername(checkUsername);
        if (!userPermit.getCoursesList().contains(student.getCoursesList().get(0))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("You cannot export this user's score report"));
        }
        return null;
    }

}

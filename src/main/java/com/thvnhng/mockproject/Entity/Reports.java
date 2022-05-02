package com.thvnhng.mockproject.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reports")
//@Data
@Getter
@Setter
public class Reports extends AbstractEntity{

    @Column(name = "report_name")
    private String reportName;
    @Column(name = "mark_15m")
    private Integer mark15m;
    @Column(name = "mark_45m")
    private Integer mark45m;
    @Column(name = "mark_final")
    private Integer markFinal;
    @Column(name = "mark_summary")
    private Double markSummary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

}

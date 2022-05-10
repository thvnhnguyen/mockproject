package com.thvnhng.mockproject.Entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "scores")
//@Data
@Getter
@Setter
public class Scores extends AbstractEntity{

    @Column(name = "report_name")
    private String scoreName;

    @Column(name = "score")
    private Double score;

    @Column(name = "score_type", nullable = false)
    private String scoreType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

    @Transient
    public String getScoreToList() {
        return score.toString() +
                " " +
                scoreType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

package com.thvnhng.mockproject.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subjects")
//@Data
@Getter
@Setter
public class Subjects extends AbstractEntity{

    @Column(name = "subject_name")
    private String subjectName;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Scores> scoresList;

    @OneToMany(mappedBy = "subjectUser", fetch = FetchType.LAZY)
    private List<Users> usersList;

}

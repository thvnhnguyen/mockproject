package com.thvnhng.mockproject.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "subjects")
//@Data
@Getter
@Setter
public class Subjects extends AbstractEntity{

    @Column(name = "subject_name")
    private String subjectName;

    @OneToMany(mappedBy = "subject")
    private List<Scores> scoresList;

    @OneToMany(mappedBy = "subjectUser")
    private List<Users> usersList;

}

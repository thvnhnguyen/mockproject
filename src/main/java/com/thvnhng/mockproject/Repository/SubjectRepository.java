package com.thvnhng.mockproject.Repository;

import com.thvnhng.mockproject.Entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subjects, Long> {

    Subjects findByIdAndDeletedAtIsNull(Long id);
    Subjects findBySubjectName(String subjectName);
    List<Subjects> findAllBySubjectNameContaining(String subjectName);
}

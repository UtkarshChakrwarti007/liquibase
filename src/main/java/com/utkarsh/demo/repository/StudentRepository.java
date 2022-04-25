package com.utkarsh.demo.repository;

import com.utkarsh.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentsByCoursesId(Long courseId);
}

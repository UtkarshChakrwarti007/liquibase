package com.utkarsh.demo.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "studentName")
    private String studentName;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "student_courses",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") })
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }
    public Student(String studentName) {
        this.studentName = studentName;

    }
    // getters and setters

    public long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }



    public Set<Course> getCourses() {
        return courses;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(long courseId) {
        Course course = this.courses.stream().filter(t -> t.getId() == courseId).findFirst().orElse(null);
        if (course != null) this.courses.remove(course);
        course.getStudents().remove(this);
    }
}
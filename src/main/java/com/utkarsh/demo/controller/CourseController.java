package com.utkarsh.demo.controller;

import com.utkarsh.demo.entity.Course;
import com.utkarsh.demo.entity.Student;
import com.utkarsh.demo.exception.ResourceNotFoundException;
import com.utkarsh.demo.repository.CourseRepository;
import com.utkarsh.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        courseRepository.findAll().forEach(courses::add);
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<Course>> getAllCoursesByStudentId(@PathVariable(value = "studentId") Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Not found Student with id = " + studentId);
        }
        List<Course> courses = courseRepository.findCoursesByStudentsId(studentId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCoursesById(@PathVariable(value = "id") Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Course with id = " + id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<Student>> getAllStudentsByCourseId(@PathVariable(value = "courseId") Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Not found Course  with id = " + courseId);
        }
        List<Student> students = studentRepository.findStudentsByCoursesId(courseId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    @PostMapping("/students/{studentId}/courses")
    public ResponseEntity<Course> addCourse(@PathVariable(value = "studentId") Long studentId, @RequestBody Course courseRequest) {
        Course course = studentRepository.findById(studentId).map(student -> {
            long courseId = courseRequest.getId();

            // course is existed
            if (courseId != 0L) {
                Course _course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Course with id = " + courseId));
                student.addCourse(_course);
                studentRepository.save(student);
                return _course;
            }

            // add and create new Course
            student.addCourse(courseRequest);
            return courseRepository.save(courseRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Student with id = " + studentId));
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }
    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long id, @RequestBody Course courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseId " + id + "not found"));
        course.setName(courseRequest.getName());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<HttpStatus> deleteCourseFromStudent(@PathVariable(value = "studentId") Long studentId, @PathVariable(value = "courseId") Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Student with id = " + studentId));

        student.removeCourse(courseId);
        studentRepository.save(student);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        courseRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
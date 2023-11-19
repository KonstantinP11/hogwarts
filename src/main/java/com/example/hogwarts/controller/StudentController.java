package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.service.StudentService;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/{id}")
    public Student read(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable long id) {
        return studentService.removeStudent(id);
    }

    @GetMapping
    public Collection<Student> getStudentByAge(@RequestParam int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping("/age")
    public Collection<Student> getAllStudentAgeBetweenMinAndMax(
            @RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.getAllStudentAgeBetweenMinAndMax(minAge, maxAge);
    }

    @GetMapping("/studentId")
    public Faculty StudentFaculty(@RequestParam long studentId) {
        return studentService.studentFaculty(studentId);
    }

    @GetMapping("/facultyId")
    public Collection<Student> readByFacultyId(@RequestParam long facultyId) {
        return studentService.readByFacultyId(facultyId);
    }
}
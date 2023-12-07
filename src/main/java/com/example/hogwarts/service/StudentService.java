package com.example.hogwarts.service;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;

import java.util.Collection;

public interface StudentService {
    Student addStudent(Student student);

    Student getStudent(long id);

    Student updateStudent(Student student);

    Student removeStudent(long id);

    Collection<Student> getStudentByAge(int age);

    Collection<Student> getAllStudentAgeBetweenMinAndMax(int minAge, int maxAge);

    Faculty studentFaculty(long studentId);

    Collection<Student> readByFacultyId(long facultyId);

    Collection<String> getFilterByName();

    Double getAllStudentsAvgAge();

    void getNamesInStreams();

    void getNamesInStreamsSynchronized();
}

package com.example.hogwarts.service;

import com.example.hogwarts.exception.StudentNotFoundException;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final StudentRepository studentRepository;
    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    @Override
    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    @Override
    public Student getStudent(long id) {
        logger.info("Was invoked method for get student");
        return studentRepository.findById(id)
                .orElseThrow(() ->
//                        logger.error("Студент не найден в хранилище");
                        new StudentNotFoundException("Студент не найден в хранилище"));
    }

    @Override
    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student");
        getStudent(student.getId());
        return studentRepository.save(student);
    }

    @Override
    public Student removeStudent(long id) {
        logger.info("Was invoked method for remove student");
        Student student = getStudent(id);
        studentRepository.delete(student);
        return student;
    }

    @Override
    public Collection<Student> getStudentByAge(int age) {
        logger.info("Was invoked method for get students by age");
        return studentRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> getAllStudentAgeBetweenMinAndMax(int minAge, int maxAge) {
        logger.info("Was invoked method for get all students specified age");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty studentFaculty(long studentId) {
        logger.info("Was invoked method for get student faculty");
        return getStudent(studentId).getFaculty();
    }

    @Override
    public Collection<Student> readByFacultyId(long facultyId) {
        logger.info("Was invoked method for get students by faculty");
        return studentRepository.findAllByFaculty_id(facultyId);
    }
}
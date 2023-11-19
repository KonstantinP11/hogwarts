package com.example.hogwarts.controller;

import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;
    @Autowired
    StudentRepository studentRepository;
    String baseUrl;
    Student student = new Student(1L, "Harry", 10);

    @BeforeEach
    void beforeEach() {
        baseUrl = "http://localhost:" + port + "/student";
        studentRepository.deleteAll();
    }

    @Test
    void create_shouldReturnStudentAndStatus200() {
        ResponseEntity<Student> result = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(student, result.getBody());
    }

    @Test
    void read_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.getForEntity(
                baseUrl + "/" + student.getId(),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Студент не найден в хранилище",
                result.getBody());
    }

    @Test
    void update_shouldReturnStudentAndStatus200() {
        Student saveStudent = studentRepository.save(student);
        ResponseEntity<Student> result = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(saveStudent),
                Student.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(saveStudent, result.getBody());
    }

    @Test
    void delete_shouldReturnStudent() {
        Student saveStudent = studentRepository.save(student);
        ResponseEntity<Student> result = restTemplate.exchange(
                baseUrl + "/" + saveStudent.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(saveStudent),
                Student.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(saveStudent, result.getBody());
    }

    @Test
    void getStudentByAge_shouldReturnStudentCollectionAndStatus200() {
        Student ron = new Student(2L, "Ron", 10);
        Student saveStudent = studentRepository.save(student);
        Student saveRon = studentRepository.save(ron);

        ResponseEntity<List<Student>> result = restTemplate.exchange(
                baseUrl + "?age=" + saveStudent.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(saveStudent, saveRon), result.getBody());
    }

}
package com.example.hogwarts.controller;

import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import com.example.hogwarts.service.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @SpyBean
    StudentServiceImpl studentService;
    @MockBean
    StudentRepository studentRepository;
    Student student = new Student(1L, "Harry", 10);
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void create_shouldReturnStudentAndStatus200() throws Exception {
        when(studentRepository.save(student)).thenReturn(student);
        mockMvc.perform(post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(student));
    }

    @Test
    void read_shouldReturnStatus404() throws Exception {
        when((studentRepository.findById(student.getId())))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/student/" + student.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Студент не найден в хранилище"));
    }

    @Test
    void update_shouldReturnStudentAndStatus200() throws Exception {
        Student student1 = new Student(1L, "Ron", 10);
        when((studentRepository.findById(student.getId())))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(student1)).thenReturn(student1);

        mockMvc.perform(put("/student")
                        .content(objectMapper.writeValueAsString(student1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(student1));
    }

    @Test
    void delete_shouldReturnStatus404() throws Exception {
        when((studentRepository.findById(student.getId())))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/student/" + student.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Студент не найден в хранилище"));
    }

    @Test
    void getStudentByAge_shouldReturnStudentCollectionAndStatus200() throws Exception {
        when(studentRepository.findAllByAge(student.getAge())).thenReturn(List.of(student, student));

        mockMvc.perform(get("/student?age=" + student.getAge()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(student))
                .andExpect(jsonPath("$.[1]").value(student));
    }
}
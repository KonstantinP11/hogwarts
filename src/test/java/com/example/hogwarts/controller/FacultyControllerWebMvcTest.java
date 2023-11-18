package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.service.FacultyServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @SpyBean
    FacultyServiceImpl facultyService;
    @MockBean
    FacultyRepository facultyRepository;
    Faculty faculty = new Faculty(1L, "Gryfindor", "red");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void create_shouldReturnFacultyAndStatus200() throws Exception {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(faculty));
    }

    @Test
    void read_shouldReturnStatus404() throws Exception {
        when((facultyRepository.findById(faculty.getId())))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/faculty/" + faculty.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Факультет не найден в хранилище"));
    }

    @Test
    void update_shouldReturnFacultyAndStatus200() throws Exception {
        Faculty faculty1 = new Faculty(1L, "22222", "green");
        when((facultyRepository.findById(faculty.getId())))
                .thenReturn(Optional.of(faculty));
        when(facultyRepository.save(faculty1)).thenReturn(faculty1);

        mockMvc.perform(put("/faculty")
                .content(objectMapper.writeValueAsString(faculty1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(faculty1));
    }
    @Test
    void delete_shouldReturnStatus404() throws Exception {
        when((facultyRepository.findById(faculty.getId())))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/faculty/" + faculty.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Факультет не найден в хранилище"));
    }

    @Test
    void readByColor_shouldReturnFacultiesCollectionAndStatus200() throws Exception {
        when(facultyRepository.findAllByColor(faculty.getColor())).thenReturn(List.of(faculty, faculty));

        mockMvc.perform(get("/faculty?color=" + faculty.getColor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(faculty))
                .andExpect(jsonPath("$.[1]").value(faculty));
    }

}
package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FacultyControllerRestTemplateTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;
    @Autowired
    FacultyRepository facultyRepository;
    String baseUrl;
    Faculty faculty = new Faculty(1L, "Grifindor", "red");

    @BeforeEach
    void beforeEach() {
        baseUrl = "http://localhost:" + port + "/faculty";
        facultyRepository.deleteAll();
    }

    @Test
    void create_shouldReturnFacultyAndStatus200() {
        ResponseEntity<Faculty> result = restTemplate.postForEntity(
                baseUrl,
                faculty,
                Faculty.class
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(faculty, result.getBody());

    }

    @Test
    void read_shouldReturnStatus404() {
        ResponseEntity<String> result = restTemplate.getForEntity(
                baseUrl + "/" + faculty.getId(),
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Факультет не найден в хранилище",
                result.getBody());
    }

    @Test
    void update_shouldReturnFacultyAndStatus200() {
        Faculty saveFaculty = facultyRepository.save(faculty);
        ResponseEntity<Faculty> result = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(saveFaculty),
                Faculty.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(saveFaculty, result.getBody());
    }

    @Test
    void delete_shouldReturnStatus404() {
        Faculty saveFaculty = facultyRepository.save(faculty);
        ResponseEntity<Faculty> result = restTemplate.exchange(
                baseUrl + "/" + saveFaculty.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(saveFaculty),
                Faculty.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(saveFaculty, result.getBody());
    }

    @Test
    void readByColor_shouldReturnFacultiesCollectionAndStatus200() {
        Faculty saveFaculty = facultyRepository.save(faculty);
        Faculty faculty1 = new Faculty(2L, "newGrifindor", "red");
        Faculty saveFaculty1 = facultyRepository.save(faculty1);
        ResponseEntity<List<Faculty>> result = restTemplate.exchange(
                baseUrl + "?color=" + faculty.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(saveFaculty, saveFaculty1), result.getBody());
    }
}

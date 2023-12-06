package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.stream.Stream;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;


    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/{id}")
    public Faculty read(@PathVariable long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public Faculty delete(@PathVariable long id) {
        return facultyService.removeFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> getFacultyByColor(@RequestParam String color) {
        return facultyService.getFacultyByColor(color);
    }

    @GetMapping("/faculty")
    public Collection<Faculty> findAllyByNameIgnoreCaseOrByColorIgnoreCase(
            @RequestParam(required = false) String name, @RequestParam(required = false) String color) {
        if (name != null && !name.isBlank()) {
            return facultyService.findFacultyByNameIgnoreCase(name);
        } else {
            return facultyService.findFacultyByColorIgnoreCase(color);
        }
    }

    @GetMapping("/maxName")
    public ResponseEntity<String> getFacultyNameMaxLength() {
        return facultyService.getFacultyNameMaxLength();
    }

    @GetMapping("/sum")
    public int getSum() {
        long time = System.currentTimeMillis();

        Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b );

        time = System.currentTimeMillis() - time;
        System.out.println(time);
        return (int) time;
    }
}

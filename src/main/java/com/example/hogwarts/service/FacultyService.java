package com.example.hogwarts.service;

import com.example.hogwarts.model.Faculty;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty getFaculty(long id);

    Faculty updateFaculty(Faculty faculty);

    Faculty removeFaculty(long id);

    Collection<Faculty> getFacultyByColor(String color);

    Collection<Faculty> findFacultyByNameIgnoreCase(String name);

    Collection<Faculty>  findFacultyByColorIgnoreCase(String color);
}

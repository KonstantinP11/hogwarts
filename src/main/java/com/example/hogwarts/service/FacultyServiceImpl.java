package com.example.hogwarts.service;

import com.example.hogwarts.exception.FacultyNotFoundException;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService {
    private FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(long id) {
        logger.info("Was invoked method for get faculty");
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException("Факультет не найден в хранилище"));
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        getFaculty(faculty.getId());
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty removeFaculty(long id) {
        logger.info("Was invoked method for remove faculty");
        Faculty faculty = getFaculty(id);
        facultyRepository.delete(faculty);
        return faculty;
    }

    @Override
    public Collection<Faculty> getFacultyByColor(String color) {
        logger.info("Was invoked method for get faculty by color");
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public Collection<Faculty> findFacultyByNameIgnoreCase(String name) {
        logger.info("Was invoked method for get faculty by name ignore case");
        return facultyRepository.findAllByNameIgnoreCase(name);
    }

    @Override
    public Collection<Faculty>  findFacultyByColorIgnoreCase(String color) {
        logger.info("Was invoked method for get faculty by color ignore case");
        return facultyRepository.findAllByColorIgnoreCase(color);
    }
}
package com.example.hogwarts.repository;

import com.example.hogwarts.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {


    Collection<Faculty> findAllByColor(String color);

    Collection<Faculty> findAllByNameIgnoreCase(String name);

    Collection<Faculty> findAllByColorIgnoreCase(String color);
}

package com.example.hogwarts.repository;

import com.example.hogwarts.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudent_id(long studentId);

}

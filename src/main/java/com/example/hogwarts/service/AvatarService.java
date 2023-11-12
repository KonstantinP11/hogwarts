package com.example.hogwarts.service;

import com.example.hogwarts.model.Avatar;
import com.example.hogwarts.model.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Path saveToFile(Student student, MultipartFile avatarFile) throws IOException;

    void saveToDb(Student student, Path filePath, MultipartFile avatarFile) throws IOException;

    Avatar readFromDb(long id);

    File readFromFile(long id) throws IOException;
}

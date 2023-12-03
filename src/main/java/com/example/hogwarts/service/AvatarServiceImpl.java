package com.example.hogwarts.service;

import com.example.hogwarts.exception.AvatarNotFoundException;
import com.example.hogwarts.model.Avatar;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.AvatarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {
    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final String avatarsDir;

    public StudentService studentService;
    public AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentService studentService,
                             AvatarRepository avatarRepository,
                             @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.avatarsDir = avatarsDir;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Был вызван метод записи аватара в файл и базу данных", studentId);
        Student student = studentService.getStudent(studentId);
        Path filePath = saveToFile(student, avatarFile);
        saveToDb(student, filePath, avatarFile);
    }

    @Override
    public Path saveToFile(Student student, MultipartFile avatarFile) throws IOException {
        logger.info("Был вызван метод записи аватара в файл", student.getId());
        Path filePath = Path.of(avatarsDir, student.getId() + "."
                + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            logger.debug("аватар был записан в файл id = " + student.getId());
        }
        return filePath;
    }

    @Override
    public void saveToDb(Student student, Path filePath, MultipartFile avatarFile) throws IOException {
        logger.info("Был вызван метод записи аватара в базу данных", student.getId());
        Avatar avatar = avatarRepository.findByStudent_id(student.getId()).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    @Override
    public Avatar readFromDb(long id) {
        logger.info("Был вызван метод чтения аватара из базы данных", id);
        return avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException("Аватар не найден"));
    }

    @Override
    public File readFromFile(long id) throws IOException {
        logger.info("Был вызван метод чтения аватара из файла",id);
        Avatar avatar = readFromDb(id);
        return new File(avatar.getFilePath());
    }

    private String getExtensions(String fileName) {
        logger.info("Был вызван метод получения расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public Page<Avatar> getAllAvatars(Integer pageNo, Integer pageSize) {
        logger.info("Был вызван метод получения всех аватаров");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return avatarRepository.findAll(paging);
    }

}
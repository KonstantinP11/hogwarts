package com.example.hogwarts.service;

import com.example.hogwarts.exception.AvatarNotFoundException;
import com.example.hogwarts.model.Avatar;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.AvatarRepository;
import nonapi.io.github.classgraph.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvatarServiceImplTest {
    StudentService studentService = mock(StudentService.class);
    AvatarRepository avatarRepository = mock(AvatarRepository.class);
    String avatarDir = ".src/test/resources/avatar";
    AvatarServiceImpl avatarService = new AvatarServiceImpl(
            studentService, avatarRepository, avatarDir);
    Student student = new Student(1L, "Harry", 10);
    String fileName = "1.jpg";
    MultipartFile file = new MockMultipartFile(
            fileName, fileName, "image/jpeg ", new byte[]{});
    Avatar avatar = new Avatar(1L, avatarDir, 23_150L,
            "image/jpeg ", new byte[]{}, student);

    @Test
    void uploadAvatar_avatarSavedToDbAndDirectory() throws IOException {

        when(studentService.getStudent(student.getId())).thenReturn(student);
        when(avatarRepository.findByStudent_id(student.getId()))
                .thenReturn(Optional.empty());

        avatarService.uploadAvatar(student.getId(), file);

        verify(avatarRepository, times(1)).save(any());
        assertTrue(FileUtils.canRead(new File(avatarDir + "/"
                + student.getId() + "." + fileName.substring(
                fileName.lastIndexOf(".") + 1))));
    }

    @Test
    void readFromDb_shouldReturnAvatar() {
        when(avatarRepository.findByStudent_id(student.getId()))
                .thenReturn(Optional.of(avatar));
        Avatar result = avatarService.readFromDb(student.getId());
        assertEquals(avatar, result);
    }

    @Test
    void readFromDb_shouldThrowExceptionWhenAvatarNotFound() {
        when(avatarRepository.findById(student.getId()))
                .thenReturn(Optional.empty());
        assertThrows(AvatarNotFoundException.class,
                () -> avatarService.readFromDb(student.getId()));
    }

    @Test
    void readFromFile_shouldReturnAvatar() throws IOException {
        when(avatarRepository.findByStudent_id(student.getId()))
                .thenReturn(Optional.of(avatar));
        File result = new File(avatar.getFilePath());
        assertEquals(avatarService.readFromFile(student.getId()), result);
    }
}
package com.example.hogwarts.controller;

import com.example.hogwarts.model.Avatar;
import com.example.hogwarts.service.AvatarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;
    private final int maxFileSizeInB = 300 * 1024;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @RequestParam Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > maxFileSizeInB) {
            return ResponseEntity.badRequest().body("Изображение слишком большое");
        }

        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().body("Изображение загружено");
    }

    @GetMapping("/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable long id) {
        Avatar avatar = avatarService.readFromDb(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) throws IOException {
        File avatar = avatarService.readFromFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Files.probeContentType(avatar.toPath())));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(Files.readAllBytes(avatar.toPath()));
    }
}
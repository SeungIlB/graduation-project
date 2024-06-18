package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import com.graduation.graduationproject.entity.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
        try {
            imageService.saveImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Image>> getImagesByUserId(@PathVariable Long userId) {
        List<Image> images = imageService.getImagesByUserId(userId);
        if (!images.isEmpty()) {
            return ResponseEntity.ok(images);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
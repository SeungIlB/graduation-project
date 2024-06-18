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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/predict/{userId}")
    public Map<String, Object> predict(@PathVariable("userId") Long userId, @RequestParam("season") String season, @RequestParam("image") MultipartFile image) throws Exception {
        return imageService.predict(userId, season, image);
    }

    @GetMapping("/random")
    public Optional<Image> getRandomImage() {
        return imageService.getRandomImage();
    }
}
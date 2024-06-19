package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.service.ImageService;
import com.graduation.graduationproject.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/predict/{userId}/{season}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> predict(@PathVariable("userId") Long userId,
                                                       @PathVariable("season") String season,
                                                       @RequestParam("image") MultipartFile image) throws Exception {
        Map<String, Object> result = imageService.predict(userId, season, image);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/random")
    public ResponseEntity<Optional<Image>> getRandomImage() {
        Optional<Image> image = imageService.getRandomImage();
        return ResponseEntity.ok(image);
    }
}
package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.dto.ImageDto;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.ImageRepository;
import com.graduation.graduationproject.repository.UserDetailsImpl;
import com.graduation.graduationproject.service.ImageService;
import com.graduation.graduationproject.entity.Image;
import com.graduation.graduationproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

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
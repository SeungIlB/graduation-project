package com.graduation.graduationproject.service;

import com.graduation.graduationproject.entity.Image;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.ImageRepository;
import com.graduation.graduationproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class ImageService {

    private final RestTemplate restTemplate;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public ImageService(@Qualifier("customRestTemplate") RestTemplate restTemplate, ImageRepository imageRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> predict(Long userId, String season, MultipartFile image) throws Exception {
        String url = "http://localhost:5000/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("season", season);
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> result = response.getBody();
            if (result != null && result.containsKey("class")) {
                saveImage(userId, image, season, (String) result.get("class"));
                return result;
            } else {
                throw new Exception("No response body");
            }
        } else {
            throw new Exception("Prediction failed");
        }
    }

    private void saveImage(Long userId, MultipartFile image, String season, String predictedClass) throws Exception {
        String filename = image.getOriginalFilename();
        String filepath = uploadDir + File.separator + filename;

        File file = new File(filepath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(image.getBytes());
        } catch (IOException e) {
            throw new Exception("Failed to save image", e);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new Exception("User not found");
        }

        Image img = new Image();
        img.setUser(userOptional.get());
        img.setFilename(filename);
        img.setSeason(season);
        img.setPredictedClass(predictedClass);
        img.setFilepath(filepath);
        img.setImageData(image.getBytes());
        imageRepository.save(img);
    }

    public Optional<Image> getRandomImage() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            return Optional.empty();
        }
        Random rand = new Random();
        return Optional.of(images.get(rand.nextInt(images.size())));
    }
}
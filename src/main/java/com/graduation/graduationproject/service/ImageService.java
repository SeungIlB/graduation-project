package com.graduation.graduationproject.service;

import com.graduation.graduationproject.entity.Image;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.ImageRepository;
import com.graduation.graduationproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setData(file.getBytes());
        image.setUser(user);

        imageRepository.save(image);
    }

    public List<Image> getImagesByUserId(Long userId) {
        return imageRepository.findByUserId(userId);
    }
}
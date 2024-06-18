package com.graduation.graduationproject.repository;

import com.graduation.graduationproject.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUserId(Long userId);
}
package com.graduation.graduationproject.repository;

import com.graduation.graduationproject.dto.LabelDto;
import com.graduation.graduationproject.entity.Label;
import com.graduation.graduationproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findById(Long id);
}
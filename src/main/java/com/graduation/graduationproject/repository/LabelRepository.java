package com.graduation.graduationproject.repository;

import com.graduation.graduationproject.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findById(Long id);
    Label findByUserId(Long id);
    @Query("SELECT l FROM label l WHERE l.id <> ?1")
    List<Label> findAllExceptUserId(Long userId);}
package com.graduation.graduationproject.entity;


import lombok.Setter;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
public class Image {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String filename;

    private String season;

    private String predictedClass;

    private String filepath; // 파일 절대 경로 필드 추가

    @Lob
    private byte[] imageData;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
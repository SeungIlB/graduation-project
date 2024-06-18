package com.graduation.graduationproject.entity;


import lombok.Setter;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Image {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] data;

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
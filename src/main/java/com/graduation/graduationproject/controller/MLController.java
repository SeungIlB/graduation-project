package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ml")
public class MLController {

    @Autowired
    private MLService mlService;

    @PostMapping("/predict/{season}")
    public Map<String, Object> predict(@PathVariable String season, @RequestParam("file") MultipartFile file) throws IOException {
        return mlService.getPrediction(file, season);
    }
}
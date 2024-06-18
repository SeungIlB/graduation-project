package com.graduation.graduationproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class MLService {

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> getPrediction(MultipartFile file, String season) throws IOException {
        String url = "http://localhost:5000/predict/" + season;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        return responseEntity.getBody();
    }
}

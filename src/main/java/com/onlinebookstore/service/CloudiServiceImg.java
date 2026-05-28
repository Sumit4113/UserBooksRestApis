package com.onlinebookstore.service;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudiServiceImg {

    private final Cloudinary cloudinary;

    public CloudiServiceImg(
            @Value("${cloudinary.cloud_name}") 
            String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dypxgh3ut",
                "api_key", "472588728299629",
                "api_secret", "XxtUs98X7MeTxzVsSGrJCPSlJi4"
        ));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", "image",
            "folder", "image_uploads"
        ));
        return uploadResult.get("secure_url").toString();
    }

    public String uploadPdf(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", "raw",
            "folder", "pdf_uploads"
        ));
        return uploadResult.get("secure_url").toString();
    }

}


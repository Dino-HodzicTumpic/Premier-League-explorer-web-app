package com.dino.plExplorer.service;

import com.cloudinary.Cloudinary;
import com.dino.plExplorer.dto.ImageUploadResult;
import lombok.AllArgsConstructor;

import java.util.Optional;


public interface ImageService {
   public Optional<ImageUploadResult> uploadImage(String imageUrl, String folder);
}

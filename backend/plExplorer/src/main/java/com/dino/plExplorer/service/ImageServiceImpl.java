package com.dino.plExplorer.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dino.plExplorer.dto.ImageUploadResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public Optional<ImageUploadResult> uploadImage(String imageUrl, String folder) {
        try {
            @SuppressWarnings("unchecked")
        Map<String, Object> result = cloudinary.uploader().upload(imageUrl, ObjectUtils.asMap(
                "folder", folder // naziv foldera "players" ili "coaches"
        ));
        String publicId = result.get("public_id").toString();
        String secureUrl = result.get("secure_url").toString();

        log.info("Uploaded image {} with publicId {}", secureUrl, publicId);

        return Optional.of(new ImageUploadResult(secureUrl, publicId));

        } catch(Exception e) {
            log.error("Error uploading image: {}", imageUrl, e);
            return  Optional.empty();
        }
    }
}

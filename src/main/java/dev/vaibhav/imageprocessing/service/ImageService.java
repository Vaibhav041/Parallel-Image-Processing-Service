package dev.vaibhav.imageprocessing.service;

import dev.vaibhav.imageprocessing.entity.Image;
import dev.vaibhav.imageprocessing.exception.ImageNotFountException;
import dev.vaibhav.imageprocessing.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Long saveImage(byte[] imageData, String filename, String contentType) {
        Image image = Image.builder()
                .imageData(imageData)
                .filename(filename)
                .contentType(contentType)
                .build();

        return imageRepository.save(image).getId();
    }


    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFountException("Image not found"));
    }
}

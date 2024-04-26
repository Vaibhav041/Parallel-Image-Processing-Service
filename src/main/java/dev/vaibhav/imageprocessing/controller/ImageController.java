package dev.vaibhav.imageprocessing.controller;

import dev.vaibhav.imageprocessing.service.IImageProcessingService;
import dev.vaibhav.imageprocessing.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    @Qualifier("parallelImageProcessingService")
    private IImageProcessingService imageProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<Long> upload(@RequestParam("file")MultipartFile file) {
        try {
            Long imageId = imageService.saveImage(file.getBytes(), file.getOriginalFilename(), file.getContentType());
            return new ResponseEntity<>(imageId, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<String> filter(@RequestParam("imageId") Long imageId, @RequestParam("filterType") String filterType) {
        try {
            return new ResponseEntity<>(imageProcessingService.applyFilter(imageId, filterType), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
  }
}






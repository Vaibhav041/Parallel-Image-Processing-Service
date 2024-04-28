package dev.vaibhav.imageprocessing.service;

import dev.vaibhav.imageprocessing.filter.BrightnessFilter;
import dev.vaibhav.imageprocessing.filter.Filter;
import dev.vaibhav.imageprocessing.filter.GrayScaleFilter;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static dev.vaibhav.imageprocessing.utils.ImageUtil.*;

@Service("imageProcessingService")
public class ImageProcessingService implements IImageProcessingService {
    private final Map<String, Filter> filters = new HashMap<>();
    private final ImageService imageService;
    @Autowired
    public ImageProcessingService(GrayScaleFilter grayScaleFilter, BrightnessFilter brightnessFilter, ImageService imageService) {
        filters.put("grayscale", grayScaleFilter);
        filters.put("brightness", brightnessFilter);
        this.imageService = imageService;
    }

    @Override
    public String applyFilter(Long imageId, String filterType) throws Exception {
        byte[] imageData = imageService.getImageById(imageId).getImageData();
        Filter filter = filters.get(filterType);
        if (filter == null) {
            return null;
        }
        BufferedImage filteredImage = filter.applyFilter(Imaging.getBufferedImage(imageData));
        byte[] grayImageData = bufferedImageToByteArray(filteredImage);
        return Base64.getEncoder().encodeToString(grayImageData);
    }
}

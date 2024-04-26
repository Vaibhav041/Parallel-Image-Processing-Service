package dev.vaibhav.imageprocessing.service;

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
    Map<String, Filter> filters = new HashMap<>();
    ImageService imageService;
    @Autowired
    ImageProcessingService(GrayScaleFilter grayScaleFilter, ImageService imageService) {
        filters.put("grayscale", grayScaleFilter);
        this.imageService = imageService;
    }

    @Override
    public String applyFilter(Long imageId, String filterType) throws IOException {
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

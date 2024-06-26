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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static dev.vaibhav.imageprocessing.utils.ImageUtil.*;

@Service("parallelImageProcessingService")
public class ParallelImageProcessingService implements IImageProcessingService {
    private final Map<String, Filter> filters = new HashMap<>();
    private final ImageService imageService;
    @Autowired
    public ParallelImageProcessingService(GrayScaleFilter grayScaleFilter, BrightnessFilter brightnessFilter, ImageService imageService) {
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
        BufferedImage image = Imaging.getBufferedImage(imageData);
        BufferedImage[] imageSegments = splitImage(image, 2, 2);
        BufferedImage[] processedImages = new BufferedImage[imageSegments.length];

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < imageSegments.length; i++) {
            final int index = i;
            executorService.execute(() -> processedImages[index] = filter.applyFilter(imageSegments[index]));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        BufferedImage filteredImage = mergeImageParts(processedImages, 2, 2, filterType.equals("grayscale"));
        byte[] filteredImageData = bufferedImageToByteArray(filteredImage);
        return Base64.getEncoder().encodeToString(filteredImageData);
    }
}

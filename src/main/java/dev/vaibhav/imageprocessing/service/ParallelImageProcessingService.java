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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static dev.vaibhav.imageprocessing.utils.ImageUtil.*;

@Service("parallelImageProcessingService")
public class ParallelImageProcessingService implements IImageProcessingService {
    Map<String, Filter> filters = new HashMap<>();
    ImageService imageService;
    @Autowired
    ParallelImageProcessingService(GrayScaleFilter grayScaleFilter, ImageService imageService) {
        filters.put("grayscale", grayScaleFilter);
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
            executorService.execute(() -> {
                try {
                    processedImages[index] = filter.applyFilter(imageSegments[index]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        BufferedImage filteredImage = mergeImageParts(processedImages, 2, 2);
        byte[] filteredImageData = bufferedImageToByteArray(filteredImage);
        return Base64.getEncoder().encodeToString(filteredImageData);
    }
}

package dev.vaibhav.imageprocessing.filter;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class BrightnessFilter implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        int amount = (int)(30 * 255 / 100);

        BufferedImage brightenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int red = Math.min(((rgb >> 16) & 0xFF) + amount, 255);
                int green = Math.min(((rgb >> 8) & 0xFF) + amount, 255);
                int blue = Math.min((rgb & 0xFF) + amount, 255);
                int pixel = (255 << 24) | (red << 16) | (green << 8) | blue;
                brightenedImage.setRGB(x, y, pixel);
            }
        }
        return brightenedImage;
    }
}

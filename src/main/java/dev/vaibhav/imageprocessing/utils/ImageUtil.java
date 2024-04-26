package dev.vaibhav.imageprocessing.utils;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    private ImageUtil() {}

    public static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Imaging.writeImage(image, outputStream, ImageFormats.GIF);
        return outputStream.toByteArray();
    }

    public static BufferedImage[] splitImage(BufferedImage image, int numRows, int numCols) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int partWidth = (imageWidth + numCols - 1) / numCols;
        int partHeight = (imageHeight + numRows - 1) / numRows;
        BufferedImage[] parts = new BufferedImage[numRows * numCols];
        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int startX = col * partWidth;
                int startY = row * partHeight;

                int width = Math.min(partWidth, imageWidth - startX);
                int height = Math.min(partHeight, imageHeight - startY);

                BufferedImage part = image.getSubimage(startX, startY, width, height);
                parts[index++] = part;
            }
        }
        return parts;
    }


    public static BufferedImage mergeImageParts(BufferedImage[] parts, int numRows, int numCols, boolean typeGray) {
        int partWidth = parts[0].getWidth();
        int partHeight = parts[0].getHeight();
        int mergedWidth = numCols * partWidth;
        int mergedHeight = numRows * partHeight;

        BufferedImage mergedImage = new BufferedImage(mergedWidth, mergedHeight, typeGray ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();

        for (int i = 0; i < parts.length; i++) {
            int row = i / numCols;
            int col = i % numCols;
            int x = col * partWidth;
            int y = row * partHeight;
            g2d.drawImage(parts[i], x, y, null);
        }
        g2d.dispose();
        return mergedImage;
    }
}

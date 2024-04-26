package dev.vaibhav.imageprocessing.filter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface Filter {
    BufferedImage applyFilter(BufferedImage image) throws IOException;
}

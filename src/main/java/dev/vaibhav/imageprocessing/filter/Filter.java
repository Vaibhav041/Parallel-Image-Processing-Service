package dev.vaibhav.imageprocessing.filter;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage applyFilter(BufferedImage image);
}

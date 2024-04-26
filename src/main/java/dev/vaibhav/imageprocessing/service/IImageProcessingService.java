package dev.vaibhav.imageprocessing.service;


public interface IImageProcessingService {
    String applyFilter(Long imageId, String filterType) throws Exception;
}

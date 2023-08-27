package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.ImageDto;

import java.util.List;

public interface ImageService {
    ImageDto getImage(int id);

    List<ImageDto> getAllImages();

    ImageDto createImage(ImageDto imageDto);

    ImageDto updateImage(ImageDto imageDto);

    void deleteImage(int id);
}

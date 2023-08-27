package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.ImageDto;
import by.teachmeskills.shopwebservice.dto.converters.ImageConverter;
import by.teachmeskills.shopwebservice.entities.Image;
import by.teachmeskills.shopwebservice.repositories.ImageRepository;
import by.teachmeskills.shopwebservice.services.ImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageConverter imageConverter;

    public ImageServiceImpl(ImageRepository imageRepository, ImageConverter imageConverter) {
        this.imageRepository = imageRepository;
        this.imageConverter = imageConverter;
    }

    @Override
    public ImageDto getImage(int id) {
        return imageConverter.toDto(imageRepository.findById(id));
    }

    @Override
    public List<ImageDto> getAllImages() {
        return imageRepository.findAll().stream().map(imageConverter::toDto).toList();
    }

    @Override
    public ImageDto createImage(ImageDto imageDto) {
        Image image = imageConverter.fromDto(imageDto);
        image = imageRepository.createOrUpdate(image);
        return imageConverter.toDto(image);
    }

    @Override
    public ImageDto updateImage(ImageDto imageDto) {
        Image image = Optional.ofNullable(imageRepository.findById(imageDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", imageDto.getId())));
        image.setImagePath(imageDto.getImagePath());
        image.setPrimary(imageDto.getPrimary());
        return imageConverter.toDto(imageRepository.createOrUpdate(image));
    }

    @Override
    public void deleteImage(int id) {
        imageRepository.delete(id);
    }
}

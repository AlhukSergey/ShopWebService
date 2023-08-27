package by.teachmeskills.shopwebservice.dto.converters;

import by.teachmeskills.shopwebservice.dto.ImageDto;
import by.teachmeskills.shopwebservice.entities.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageConverter {
    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imagePath(image.getImagePath())
                .primary(image.getPrimary())
                .build();
    }

    public Image fromDto(ImageDto imageDto) {
        return Image.builder()
                .imagePath(imageDto.getImagePath())
                .primary(imageDto.getPrimary())
                .build();
    }
}

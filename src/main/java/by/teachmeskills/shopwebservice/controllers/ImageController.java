package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.ImageDto;
import by.teachmeskills.shopwebservice.services.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        return new ResponseEntity<>(imageService.getAllImages(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable int id) {
        return Optional.ofNullable(imageService.getImage(id)).map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ImageDto> createImage(@RequestBody @Valid ImageDto imageDto) {
        return new ResponseEntity<>(imageService.createImage(imageDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ImageDto> updateImage(@RequestBody @Valid ImageDto imageDto) {
        return new ResponseEntity<>(imageService.updateImage(imageDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable int id) {
        imageService.deleteImage(id);
    }
}

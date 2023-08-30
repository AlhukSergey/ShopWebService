package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.ImageDto;
import by.teachmeskills.shopwebservice.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "image", description = "Image Endpoint")
@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(
            summary = "Find all images",
            description = "Find all existed images in Shop",
            tags = {"image"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All images were found",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ImageDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Images not found - server error"
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        return new ResponseEntity<>(imageService.getAllImages(), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain image",
            description = "Find certain existed image in Shop by its id",
            tags = {"image"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image was found by its id",
                    content = @Content(schema = @Schema(contentSchema = ImageDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Image not fount - forbidden operation"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@Parameter(required = true, description = "Image ID") @PathVariable int id) {
        return Optional.ofNullable(imageService.getImage(id)).map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create image",
            description = "Create new image",
            tags = {"image"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Image was created",
                    content = @Content(schema = @Schema(contentSchema = ImageDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Image not created - server error"
            )
    })
    @PostMapping
    public ResponseEntity<ImageDto> createImage(@RequestBody @Valid ImageDto imageDto) {
        return new ResponseEntity<>(imageService.createImage(imageDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update image",
            description = "Update existed image",
            tags = {"image"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image was updated",
                    content = @Content(schema = @Schema(contentSchema = ImageDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Image not updated - server error"
            )
    })
    @PutMapping
    public ResponseEntity<ImageDto> updateImage(@RequestBody @Valid ImageDto imageDto) {
        return new ResponseEntity<>(imageService.updateImage(imageDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete image",
            description = "Delete existed image",
            tags = {"image"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Image not deleted - server error"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteImage(@Parameter(required = true, description = "Image ID") @PathVariable int id) {
        imageService.deleteImage(id);
    }
}

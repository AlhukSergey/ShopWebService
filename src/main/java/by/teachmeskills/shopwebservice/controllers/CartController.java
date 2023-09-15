package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.CartDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException;
import by.teachmeskills.shopwebservice.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "cart", description = "ShopCart Endpoint")
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Add product",
            description = "Add product to shopping cart",
            tags = {"cart"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product was added",
                    content = @Content(schema = @Schema(contentSchema = CartDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product not added - server error"
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<CartDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(cartService.addProductToCart(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product",
            description = "Delete product from shopping cart",
            tags = {"cart"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product not deleted - server error"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@Parameter(required = true, description = "Product ID") @PathVariable int id) throws EntityNotFoundException {
        cartService.removeProductFromCart(id);
    }

    @Operation(
            summary = "Clear shopping cart",
            description = "Delete all products from shopping cart",
            tags = {"cart"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping cart was cleaned"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Shopping cart not cleaned - server error"
            )
    })
    @DeleteMapping("/clearCart")
    public void clearShoppingCart() {
        cartService.clear();
    }
}

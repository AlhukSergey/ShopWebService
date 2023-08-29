package by.teachmeskills.shopwebservice.controllers;

import by.teachmeskills.shopwebservice.dto.CartDto;
import by.teachmeskills.shopwebservice.dto.ProductDto;
import by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException;
import by.teachmeskills.shopwebservice.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(cartService.addProductToCart(productDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) throws EntityNotFoundException {
        cartService.removeProductFromCart(id);
    }

    @DeleteMapping("/clearCart")
    public void clearShoppingCart() {
        cartService.clear();
    }
}

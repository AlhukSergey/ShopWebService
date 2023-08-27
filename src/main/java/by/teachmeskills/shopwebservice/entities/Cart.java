package by.teachmeskills.shopwebservice.entities;

import by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Cart {
    private final Map<Integer, Product> products;

    public Cart() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void removeProduct(int productId) throws EntityNotFoundException {
        if (!products.containsKey(productId))
            throw new EntityNotFoundException("Продукта с id %d не найдено.");
        products.remove(productId);
    }

    public void clear() {
        products.clear();
    }
}

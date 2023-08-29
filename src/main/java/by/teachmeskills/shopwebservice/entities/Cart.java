package by.teachmeskills.shopwebservice.entities;

import by.teachmeskills.shopwebservice.exceptions.EntityNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Cart {
    private final Map<Integer, Product> products;
    @Getter
    private double totalPrice;

    public Cart() {
        this.products = new HashMap<>();
        this.totalPrice = 0;
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        totalPrice += product.getPrice();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public void removeProduct(int productId) throws EntityNotFoundException {
        if (!products.containsKey(productId))
            throw new EntityNotFoundException("Продукта с id %d не найдено.");
        Product product = products.get(productId);
        products.remove(productId);
        totalPrice -= product.getPrice();
    }

    public void clear() {
        products.clear();
    }
}

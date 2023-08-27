package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Product;

import java.util.List;

public interface ProductRepository {
    Product findById(int id);

    List<Product> findAllByCategoryId(int categoryId);

    List<Product> findAllBySearchParameter(String parameter);

    List<Product> findAll();

    Product createOrUpdate(Product product);

    void delete(int id);
}

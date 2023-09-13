package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategoryId(int categoryId);

    List<Product> findAllBySearchParameter(String parameter);
}

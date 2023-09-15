package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategoryId(int categoryId);

    @Query(value = "select * from shop.products where name  = :parameter", nativeQuery = true)
    List<Product> findAllBySearchParameter(@Param("parameter") String parameter);
}

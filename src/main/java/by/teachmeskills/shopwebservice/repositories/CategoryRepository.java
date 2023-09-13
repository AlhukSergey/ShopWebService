package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
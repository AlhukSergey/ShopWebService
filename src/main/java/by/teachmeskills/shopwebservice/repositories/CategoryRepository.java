package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.Category;

import java.util.List;

public interface CategoryRepository {
    Category findById(int id);

    List<Category> findAll();

    Category createOrUpdate(Category category);

    void delete(int id);
}
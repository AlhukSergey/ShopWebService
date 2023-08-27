package by.teachmeskills.shopwebservice.repositories.impl;

import by.teachmeskills.shopwebservice.entities.Category;
import by.teachmeskills.shopwebservice.repositories.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class, id);
    }

    @Override
    public List<Category> findAll() {
        return entityManager.createQuery("select c from Category c", Category.class).getResultList();
    }

    @Override
    public Category createOrUpdate(Category entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Category category = Optional.ofNullable(entityManager.find(Category.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категории с id %d не найдено.", id)));
        entityManager.remove(category);
    }
}

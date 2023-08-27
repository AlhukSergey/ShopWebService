package by.teachmeskills.shopwebservice.repositories.impl;

import by.teachmeskills.shopwebservice.entities.Product;
import by.teachmeskills.shopwebservice.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public Product createOrUpdate(Product entity) {
        return entityManager.merge(entity);
    }

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("select p from Product p ", Product.class).getResultList();
    }

    @Override
    public void delete(int id) {
        Product product = Optional.ofNullable(entityManager.find(Product.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продукта с id %d не найдено.", id)));
        entityManager.remove(product);
    }

    @Override
    public List<Product> findAllByCategoryId(int categoryId) {
        return entityManager.createQuery("select p from Product p where p.category.id=:category_id", Product.class)
                .setParameter("category_id", categoryId).getResultList();
    }

    @Override
    public List<Product> findAllBySearchParameter(String parameter) {
        return entityManager.createQuery("select p from Product p where lower(p.name) like lower(:parameter) " +
                        "or lower(p.description) like lower(:parameter) order by name", Product.class)
                .setParameter("parameter", "%" + parameter.toLowerCase() + "%").getResultList();
    }
}

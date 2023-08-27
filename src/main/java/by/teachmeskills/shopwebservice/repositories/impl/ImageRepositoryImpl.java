package by.teachmeskills.shopwebservice.repositories.impl;

import by.teachmeskills.shopwebservice.entities.Image;
import by.teachmeskills.shopwebservice.repositories.ImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Image createOrUpdate(Image entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        Image image = Optional.ofNullable(entityManager.find(Image.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Изображение с id %d не найдено.", id)));
        entityManager.remove(image);
    }

    @Override
    public Image findById(int id) {
        return entityManager.find(Image.class, id);
    }

    @Override
    public List<Image> findAll() {
        return entityManager.createQuery("select i from Image i ", Image.class).getResultList();
    }
}

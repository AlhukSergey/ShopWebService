package by.teachmeskills.shopwebservice.repositories.impl;

import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.exceptions.LoginException;
import by.teachmeskills.shopwebservice.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createOrUpdate(User entity) {
        return entityManager.merge(entity);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u ", User.class).getResultList();
    }

    @Override
    public void delete(int id) {
        User user = Optional.ofNullable(entityManager.find(User.class, id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", id)));
        entityManager.remove(user);
    }

    @Override
    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws LoginException {
        User user;
        try {
            user = entityManager.createQuery("select u from User u where u.email=:email and u.password=:password", User.class)
                    .setParameter("email", email).setParameter("password", password).getSingleResult();
        } catch (Exception e) {
            throw new LoginException(String.format("Пользователя с логином %s и паролем %s не найдено. Пожалуйста, введите данные повторно либо перейдите регистрацию.", email, password));
        }
        return user;
    }
}

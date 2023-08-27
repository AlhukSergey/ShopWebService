package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.exceptions.LoginException;

import java.util.List;

public interface UserRepository {
    User findById(int id);

    User findByEmailAndPassword(String email, String password) throws LoginException;

    List<User> findAll();

    User createOrUpdate(User user);

    void delete(int id);
}

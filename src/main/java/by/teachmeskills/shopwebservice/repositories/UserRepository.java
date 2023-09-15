package by.teachmeskills.shopwebservice.repositories;

import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.exceptions.LoginException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password) throws LoginException;
    Optional<User> findByEmail(String email);
}

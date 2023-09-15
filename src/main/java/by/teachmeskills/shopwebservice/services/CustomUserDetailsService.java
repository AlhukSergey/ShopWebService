package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.entities.CustomUserDetails;
import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByEmail(email);
        return userEntity.map(CustomUserDetails::fromUserEntityToCustomUserDetails).orElse(null);
    }
}

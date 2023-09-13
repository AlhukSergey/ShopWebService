package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.dto.UserDto;
import by.teachmeskills.shopwebservice.dto.converters.UserConverter;
import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.exceptions.LoginException;
import by.teachmeskills.shopwebservice.repositories.UserRepository;
import by.teachmeskills.shopwebservice.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto getUser(int id) {
        return userConverter.toDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", id))));
    }

    @Override
    public UserDto getUserByEmailAndPassword(String email, String password) throws LoginException {
        return userConverter.toDto(userRepository.findByEmailAndPassword(email, password));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userConverter.fromDto(userDto);
        user = userRepository.save(user);
        return userConverter.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", userDto.getId())));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setBirthday(userDto.getBirthday());
        user.setBalance(userDto.getBalance());
        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userConverter::toDto).toList();
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", id)));
        userRepository.delete(user);
    }
}

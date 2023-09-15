package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.UserDto;
import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.exceptions.LoginException;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    UserDto getUserByEmailAndPassword(String email, String password) throws LoginException;

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    List<UserDto> getAll();

    void deleteUser(int id);

    User getByEmailAndPassword(String email, String password);

    User getByEmail(String email);

    UserDto updateUserRole(UserDto userDto);
}


package ru.kata.spring.boot_security.demo.services;


import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user);

    void updateUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User findByEmail(String email);
}
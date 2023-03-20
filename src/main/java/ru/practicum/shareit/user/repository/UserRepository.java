package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAll();

    User findUserById(int id);

    User createUser(User user);

    User updateUser(User user, int id);

    void deleteUser(int id);
}

package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAll();

    User getById(int id);

    User create(User user);

    User update(User user, int id);

    void delete(int id);
}

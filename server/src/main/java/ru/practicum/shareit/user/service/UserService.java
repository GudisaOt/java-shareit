package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    UserDto getById(int id);

    UserDto create(User user);

    UserDto update(User user, int id);

    void delete(int id);
}

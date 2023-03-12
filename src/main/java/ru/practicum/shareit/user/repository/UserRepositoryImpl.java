package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class UserRepositoryImpl implements UserRepository {
    HashMap<Integer, User> usersStorage = new HashMap<>();
    private static int genId = 1;

    @Override
    public Collection<User> getAll() {
        return usersStorage.values();
    }

    @Override
    public User findUserById(int id) {
        return usersStorage.get(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(genId);
        genId++;
        usersStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, int id) {
        usersStorage.put(user.getId(), user);
        return usersStorage.get(user.getId());
    }

    @Override
    public void deleteUser(int id) {
        usersStorage.remove(id);
    }
}

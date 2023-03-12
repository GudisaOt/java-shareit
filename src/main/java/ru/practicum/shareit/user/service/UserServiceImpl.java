package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NonUniqueException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        try {
            return userRepository.findUserById(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public User create(User user) {
        checkMail(user);
        return userRepository.createUser(user);
    }

    @Override
    public User update(User user, int id) {
        // проверка есть ли такой пользователь
        try {
           User userToUpd =  userRepository.findUserById(id);
            if (user.getEmail() != null){
                user.setId(id);
                checkMail(user);
                userToUpd.setEmail(user.getEmail());
            }
            if (user.getName() != null){
                userToUpd.setName(user.getName());
            }
            return userRepository.updateUser(userToUpd, id);
        } catch (NotFoundException e){
            e.getMessage();
        }
        return null;
    }

    @Override
    public void delete(int id) {
        try {
            userRepository.deleteUser(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
    }

    @SneakyThrows
    private void checkMail (User user){
        for (User check: userRepository.getAll()) {
            if (user.getEmail().equals(check.getEmail()) && user.getId()!=check.getId()){
                throw new NonUniqueException("Email is not unique!");
            }
        }
    }
}

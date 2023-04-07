package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("Not found!"));
    }
    @Transactional
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user, int id) {
        User userToUpd = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Optional.ofNullable(user.getName()).ifPresent(userToUpd::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(userToUpd::setEmail);
        return userRepository.save(userToUpd);
    }

    @Transactional
    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}

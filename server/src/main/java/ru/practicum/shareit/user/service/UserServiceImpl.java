package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getById(int id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found!")));
    }

    @Transactional
    @Override
    public UserDto create(User user) {
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto update(User user, int id) {
        User userToUpd = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Optional.ofNullable(user.getName()).ifPresent(userToUpd::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(userToUpd::setEmail);
        return userMapper.toUserDto(userRepository.save(userToUpd));
    }

    @Transactional
    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}

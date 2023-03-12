package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static UserDto toUserDto(User user){
        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .build();
        return userDto;
    }
}

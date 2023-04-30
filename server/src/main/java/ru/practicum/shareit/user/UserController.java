package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.create(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody User user, @PathVariable Integer id) {
        return ResponseEntity.ok().body(userService.update(user, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}

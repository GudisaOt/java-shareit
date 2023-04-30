package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserGateDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        return userClient.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserGateDto user) {
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserGateDto user, @PathVariable Integer id) {
        return userClient.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id) {
        return userClient.deleteUser(id);
    }
}

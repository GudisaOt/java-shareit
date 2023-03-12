package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id;
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
    @Builder
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

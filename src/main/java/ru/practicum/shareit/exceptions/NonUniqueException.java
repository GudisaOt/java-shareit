package ru.practicum.shareit.exceptions;

public class NonUniqueException extends RuntimeException {
    public NonUniqueException(String m) {
        super("Email is not unique!");
    }
}

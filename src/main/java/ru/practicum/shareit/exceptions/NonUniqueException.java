package ru.practicum.shareit.exceptions;

public class NonUniqueException extends Exception {
    public NonUniqueException(String m) {
        super("Email is not unique!");
    }
}

package ru.practicum.shareit.exceptions;

public class ErrorsResponse {
    private String error;

    public ErrorsResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

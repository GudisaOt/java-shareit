package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTimesDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getAllByOwner(int ownerId, String state);

    List<BookingDto> getAllByUser(int userId, String state);

    BookingDto add(BookingTimesDto bookingTimesDto, int id);

    BookingDto approve(int bookingId, int userId, Boolean approved);

    BookingDto getById(int bookingId, int userId);
}

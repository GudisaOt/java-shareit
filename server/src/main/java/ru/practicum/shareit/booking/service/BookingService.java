package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTimesDto;

import java.util.List;

@Service
public interface BookingService {
    List<BookingDto> getAllByOwner(int ownerId, String state, int from, int size);

    List<BookingDto> getAllByUser(int userId, String state, int from, int size);

    BookingDto add(BookingTimesDto bookingTimesDto, int id);

    BookingDto approve(int bookingId, int userId, Boolean approved);

    BookingDto getById(int bookingId, int userId);
}

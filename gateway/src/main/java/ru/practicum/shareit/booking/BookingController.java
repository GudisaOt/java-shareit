package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exceptions.BadRequestException;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;



@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;
    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(header) Long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String statusString,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState status = BookingState.from(statusString)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + statusString));
        return bookingClient.getBookings(userId, status, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(header) Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String statusString,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState status = BookingState.from(statusString)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + statusString));
        return bookingClient.getBookingByOwner(userId, status, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(header) Long userId, @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId,
                                             @Valid @RequestBody BookItemRequestDto bookingGateDto) {
        return bookingClient.createBooking(userId, bookingGateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(header) Long userId, @PathVariable Long bookingId,
                                              @RequestParam Boolean approved) {
        return bookingClient.approve(userId, bookingId, approved);
    }
}

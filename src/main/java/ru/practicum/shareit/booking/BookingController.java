package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllByUser(@RequestHeader(header) Integer userId,
                                      @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok().body(bookingService.getAllByUser(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getAllByOwner(@RequestHeader(header) Integer userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.getAllByOwner(userId, state));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getById(@RequestHeader(header) Integer userId, @PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.getById(bookingId, userId));
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader(header) Integer userId,
                                             @Valid @RequestBody BookingTimesDto bookingTimesDto) {
        return ResponseEntity.ok(bookingService.add(bookingTimesDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approve(@RequestHeader(header) Integer userId, @PathVariable Integer bookingId,
                                           @RequestParam Boolean approved) {
        return ResponseEntity.ok(bookingService.approve(bookingId, userId, approved));
    }
}

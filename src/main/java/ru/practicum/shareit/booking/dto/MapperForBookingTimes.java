package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class MapperForBookingTimes {
    public static BookingTimesDto stBookingTimesDto(Booking booking) {
        return BookingTimesDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}

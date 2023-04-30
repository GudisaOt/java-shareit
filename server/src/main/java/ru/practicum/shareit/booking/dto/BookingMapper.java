package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    @Mapping(target = "itemId", source = "booking.item.id")
    BookingTimesDto toBookingTimesDto(Booking booking);

    Booking toBooking(BookingTimesDto bookingTimesDto);

}

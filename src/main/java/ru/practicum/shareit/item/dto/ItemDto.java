package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private BookingTimesDto lastBooking;
    private BookingTimesDto nextBooking;
    private List<CommentDto> comments;
}

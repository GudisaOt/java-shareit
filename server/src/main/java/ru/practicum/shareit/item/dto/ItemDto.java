package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingTimesDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemDto {
    private int id;

    private String name;

    private String description;

    private Boolean available;
    private int requestId;
    private BookingTimesDto lastBooking;
    private BookingTimesDto nextBooking;
    private List<CommentDto> comments;
}

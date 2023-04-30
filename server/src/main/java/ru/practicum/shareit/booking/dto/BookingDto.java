package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private int id;

    private User booker;

    private Item item;


    private LocalDateTime start;


    private LocalDateTime end;

    private Status status;
}

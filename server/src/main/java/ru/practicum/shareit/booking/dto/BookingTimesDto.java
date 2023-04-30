package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingTimesDto {
    private int id;
    private int bookerId;
    private int itemId;

    private LocalDateTime start;


    private LocalDateTime end;
}

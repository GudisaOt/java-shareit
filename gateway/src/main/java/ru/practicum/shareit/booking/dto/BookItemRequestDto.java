package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
;
import lombok.Getter;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
}

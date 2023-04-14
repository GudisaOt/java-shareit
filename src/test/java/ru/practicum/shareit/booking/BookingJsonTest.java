package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingJsonTest {
    @Autowired
    JacksonTester<BookingDto> js;

    @Test
    void bookingJsonTest() throws IOException {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.of(2023, 7, 17,22,00,10))
                .end(LocalDateTime.of(2023,8,15,20,00,10))
                .build();

        JsonContent<BookingDto> bookingJson = js.write(bookingDto);

        assertThat(bookingJson).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(bookingJson).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2023, 7, 17,22,00,10).toString());
        assertThat(bookingJson).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2023,8,15,20,00,10).toString());

    }
}

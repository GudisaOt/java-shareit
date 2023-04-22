package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.enums.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> js;

    @Test
    void bookingDtoJsTest() throws IOException {
        BookingDto bookingDto = BookingDto.builder()
                .item(null)
                .id(1)
                .start(LocalDateTime.of(2023,12,12,12,10,10))
                .end(LocalDateTime.of(2023,12,13,12,12,10))
                .booker(null)
                .status(Status.WAITING)
                .build();

        JsonContent<BookingDto> jsonContent = js.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2023,12,12,12,10,10).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2023,12,13,12,12,10).toString());
    }
}

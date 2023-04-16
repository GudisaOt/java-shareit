package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingTimesDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingTimesDtoJsTest {
    @Autowired
    JacksonTester<BookingTimesDto> js;

    @Test
    void bookingTimesDtoJsTest() throws IOException {
        BookingTimesDto bookingTimesDto = BookingTimesDto.builder()
                .id(1)
                .start(LocalDateTime.of(2023,12,12,12,10,10))
                .end(LocalDateTime.of(2023,12,13,12,12,10))
                .build();

        JsonContent<BookingTimesDto> jsonContent = js.write(bookingTimesDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2023,12,12,12,10,10).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2023,12,13,12,12,10).toString());
    }
}

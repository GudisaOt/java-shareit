package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestJsonTest {
    @Autowired
    JacksonTester<ItemRequestDto> js;

    @Test
    void itemRequestJsonTest() throws IOException {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("desc")
                .build();

        JsonContent<ItemRequestDto> reqJson = js.write(itemRequestDto);

        assertThat(reqJson).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(reqJson).extractingJsonPathStringValue("$.description").isEqualTo("desc");
    }
}

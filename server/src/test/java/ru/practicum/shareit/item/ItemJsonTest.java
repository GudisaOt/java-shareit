package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void itemJsonTest() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        JsonContent<ItemDto> jsonItem = json.write(itemDto);

        assertThat(jsonItem).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonItem).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonItem).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(jsonItem).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}

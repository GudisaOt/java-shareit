package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemForRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemForRequestJsonTest {
    @Autowired
    private JacksonTester<ItemForRequest> js;

    @Test
    void itemForRequestJsonTest() throws IOException {
        ItemForRequest item = ItemForRequest.builder()
                .id(1)
                .name("name")
                .description("desc")
                .available(true)
                .requestId(1)
                .ownerId(2)
                .build();

        JsonContent<ItemForRequest> jsonContent = js.write(item);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}

package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    private JacksonTester<CommentDto> js;

    @Test
    void commentJsonTest() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .authorName("name")
                .text("text")
                .created(LocalDateTime.of(2023,11,12,12,10,10))
                .id(1)
                .build();

        JsonContent<CommentDto> jsonContent = js.write(commentDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName").isEqualTo("name");
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created")
                .isEqualTo(LocalDateTime.of(2023,11,12,12,10,10).toString());

    }
}

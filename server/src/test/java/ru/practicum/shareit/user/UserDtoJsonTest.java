package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> js;

    @Test
    void userDtoJsonTest() throws IOException {
        UserDto userDto = UserDto.builder()
                .email("a@mail.ru")
                .name("name")
                .build();

        JsonContent<UserDto> jsonUser = js.write(userDto);

        assertThat(jsonUser).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonUser).extractingJsonPathStringValue("$.email").isEqualTo("a@mail.ru");
    }
}

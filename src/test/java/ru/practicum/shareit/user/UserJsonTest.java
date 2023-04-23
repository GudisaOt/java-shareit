package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<User> json;

    @Test
    void userTest() throws IOException {
        User user = User.builder()
                .email("ml@mail.com")
                .name("name")
                .id(1)
                .build();

        JsonContent<User> jsonUser = json.write(user);

        assertThat(jsonUser).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonUser).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(jsonUser).extractingJsonPathStringValue("$.email").isEqualTo("ml@mail.com");
    }
}

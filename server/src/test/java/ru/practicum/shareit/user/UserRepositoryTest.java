package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("m@mail.ru")
                .build();
    }

    @Test
    void saveUserTest() {
        User saved = userRepository.save(user);

        assertEquals(1, saved.getId());
        assertEquals(user.getName(), saved.getName());
    }

    @Test
    void findUserByIdTest() {
        userRepository.save(user);
        Optional<User> found = userRepository.findById(1);

        assertEquals(user.getName(), found.get().getName());
    }
}

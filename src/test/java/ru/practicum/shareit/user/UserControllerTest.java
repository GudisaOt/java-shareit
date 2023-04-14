package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();
    }

    @Test
    void createTest() {
        ResponseEntity<User> savedUser = userController.createUser(user);
        assertEquals(savedUser.getBody().getName(), user.getName());
    }

    @Test
    void updateTest() {
        userController.createUser(user);
        User newUser = User.builder()
                .name("updIvan")
                .email("newMail@mail.com")
                .build();
        ResponseEntity<User> updatedUser = userController.updateUser(newUser, 1);
        assertEquals(newUser.getName(), updatedUser.getBody().getName());
        assertEquals(newUser.getEmail(), updatedUser.getBody().getEmail());
    }

    @Test
    void deleteTest() {
        userController.createUser(user);
        assertEquals(1, userController.getAllUsers().getBody().size());
        userController.deleteUser(1);
        assertEquals(0, userController.getAllUsers().getBody().size());
    }
}

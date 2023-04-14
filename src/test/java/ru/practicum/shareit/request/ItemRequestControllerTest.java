package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestControllerTest {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto request;

    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user")
                .email("user@mail.ru")
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build();

        request = ItemRequestDto.builder()
                .description("request")
                .build();
    }

    @Test
    void createRequestTest() {
        ResponseEntity<User> requestor = userController.createUser(user);
        ResponseEntity<ItemRequestDto> itemRequestDto = itemRequestController.create(requestor.getBody().getId(), request);

        assertEquals(request.getDescription(), itemRequestDto.getBody().getDescription());
    }

    @Test
    void getAllRequestsTest() {
        ResponseEntity<User> requestor = userController.createUser(user);
        ResponseEntity<User> user1 = userController.createUser(user2);
        ResponseEntity<ItemRequestDto> itemRequestDto = itemRequestController.create(requestor.getBody().getId(), request);

        assertEquals(1, itemRequestController.getAll(user1.getBody().getId(),0,1).getBody().size());
    }

    @Test
    void getRequestById() {
        ResponseEntity<User> requestor = userController.createUser(user);
        ResponseEntity<ItemRequestDto> itemRequestDto = itemRequestController.create(requestor.getBody().getId(), request);
        ResponseEntity<ItemRequestDto> found = itemRequestController.getById(1,requestor.getBody().getId());

        assertEquals(itemRequestDto.getBody().getDescription(), found.getBody().getDescription());
    }

    @Test
    void getAllRequestByUser() {
        ResponseEntity<User> requestor = userController.createUser(user);
        ResponseEntity<User> user1 = userController.createUser(user2);
        ResponseEntity<ItemRequestDto> itemRequestDto = itemRequestController.create(requestor.getBody().getId(), request);

        assertEquals(0, itemRequestController.getAllByUser(user1.getBody().getId()).getBody().size());
        assertEquals(1, itemRequestController.getAllByUser(requestor.getBody().getId()).getBody().size());
    }
}

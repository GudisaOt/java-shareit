package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    private ItemDto itemDto;

    private User user;

    private ItemRequest itemRequest;

    private Comment comment;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();

        user = User.builder()
                .name("user")
                .email("a@mail.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .description("desc")
                .build();

        comment = Comment.builder()
                .text("txt")
                .build();
    }

    @Test
    void createTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(itemDto.getName(), createdItem.getBody().getName());
    }

    @Test
    void updateTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        Item updatedItem = Item.builder()
                .name("upd")
                .description("upd")
                .available(false)
                .build();
        ResponseEntity<ItemDto> itemAftUpd = itemController.update(updatedItem, 1, 1);
        assertEquals(updatedItem.getName(), itemAftUpd.getBody().getName());
        assertEquals(updatedItem.getDescription(), itemAftUpd.getBody().getDescription());
        assertEquals(updatedItem.getAvailable(), itemAftUpd.getBody().getAvailable());
    }

    @Test
    void deleteTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(itemDto.getName(), createdItem.getBody().getName());
        itemController.delete(1);
        assertEquals(0, itemController.getAll(1).getBody().size());
    }

    @Test
    void getAllTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        ResponseEntity<ItemDto> createdItem2 = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(2, itemController.getAll(1).getBody().size());
    }

    @Test
    void searchTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        ResponseEntity<ItemDto> createdItem2 = itemController.create(ItemDto.builder()
                        .name("phone")
                        .description("mobile phone")
                        .available(true)
                        .build(), 1);

        assertEquals(1, itemController.search("PhoNE").getBody().size());
    }
}

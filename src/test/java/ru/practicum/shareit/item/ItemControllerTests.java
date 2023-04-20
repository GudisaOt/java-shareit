package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;


import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDto itemDto;

    private User user;

    private ItemRequestDto itemRequest;

    private Comment comment;

    private BookingTimesDto bookingTimesDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .name("item")
                .description("desc")
                .available(true)
                .requestId(1)
                .build();

        user = User.builder()
                .name("user")
                .email("a@mail.ru")
                .build();

        itemRequest = ItemRequestDto.builder()
                .description("desc")
                .build();

        comment = Comment.builder()
                .text("txt")
                .build();

        bookingTimesDto = BookingTimesDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2023,12,12,12,12,12))
                .end(LocalDateTime.of(2023,12,13,12,12,2))
                .build();
    }

    @Test
    void createTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(itemDto.getName(), createdItem.getBody().getName());
    }

    @Test
    void updateTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        itemRequestController.create(1,itemRequest);
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
        itemRequestController.create(1,itemRequest);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(itemDto.getName(), createdItem.getBody().getName());
        itemController.delete(1);
        assertEquals(0, itemController.getAll(1).getBody().size());
    }

    @Test
    void getAllTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        ResponseEntity<ItemDto> createdItem2 = itemController.create(itemDto, user1.getBody().getId());
        assertEquals(2, itemController.getAll(1).getBody().size());
    }

    @Test
    void searchTest() {
        ResponseEntity<User> user1 = userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        ResponseEntity<ItemDto> createdItem2 = itemController.create(ItemDto.builder()
                        .name("phone")
                        .description("mobile phone")
                        .available(true)
                        .build(), 1);

        assertEquals(1, itemController.search("PhoNE").getBody().size());
    }

    @Test
    void searchWhenTextIsBlank() {
        ResponseEntity<User> user1 = userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        ResponseEntity<ItemDto> createdItem = itemController.create(itemDto, user1.getBody().getId());
        ResponseEntity<ItemDto> createdItem2 = itemController.create(ItemDto.builder()
                .name("phone")
                .description("mobile phone")
                .available(true)
                .build(), 1);

        assertEquals(0, itemController.search("").getBody().size());
    }

    @Test
    void notFoundExcWhenWrongId() {
        userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        itemController.create(itemDto,1);
        assertThrows(NotFoundException.class, () -> itemController.getById(11,12));
        assertThrows(NotFoundException.class, () -> itemController.update(Item.builder()
                        .available(true)
                        .name("item")
                        .description("desc")
                        .build(),11,1));
        assertThrows(NotFoundException.class, () -> itemController.update(Item.builder()
                .available(true)
                .name("item")
                .description("desc")
                .build(),1,12));
    }

    @Test
    void validationExceptionWhenItemFieldIsNull() {
        userController.createUser(user);

        assertThrows(ConstraintViolationException.class, () -> itemController.create(new ItemDto(),1));
    }

    @Test
    void badRequestExcWhenCommentIsBlank() {
        userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        itemController.create(itemDto,1);
        comment.setText("");

        assertThrows(BadRequestException.class, () -> itemController.postComment(1,1, comment));
    }

    @Test
    void getByIdTest() {
        userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        itemController.create(itemDto,1);
        assertEquals(1, itemController.getById(1,1).getBody().getId());
    }

    @Test
    void postCommentWithEmptyBookingExc() {
        userController.createUser(user);
        itemRequestController.create(1,itemRequest);
        itemController.create(itemDto,1);

        assertThrows(BadRequestException.class, () -> itemController.postComment(1,1, comment));
    }

    @Test
    void postCommentWithIncorrectDate() {
        userController.createUser(user);
        userController.createUser(User.builder().email("a@m.ru").name("ivan").build());
        itemRequestController.create(1,itemRequest);
        itemController.create(itemDto,1);
        bookingController.create(2,bookingTimesDto);
        bookingController.approve(1,1,true);
        assertThrows(BadRequestException.class, () -> itemController.postComment(1,2,comment));
    }
}

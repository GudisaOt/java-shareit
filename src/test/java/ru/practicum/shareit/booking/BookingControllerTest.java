package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.enums.Status.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    private ItemDto itemDto;

    private User user;

    private User user1;

    private BookingTimesDto bookingTimesDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user")
                .email("user@mail.ru")
                .build();

        user1 = User.builder()
                .name("booker")
                .email("booker@mail.ru")
                .build();

        itemDto = ItemDto.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();

        bookingTimesDto = BookingTimesDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2023,7, 17, 22, 00))
                .end(LocalDateTime.of(2023, 8, 15, 20, 00))
                .build();
    }

    @Test
    void createBookingTest() {
        ResponseEntity<User> owner = userController.createUser(user);
        ResponseEntity<User> booker = userController.createUser(user1);
        ResponseEntity<ItemDto> item = itemController.create(itemDto, owner.getBody().getId());
        ResponseEntity<BookingDto> bookingDto = bookingController.create(booker.getBody().getId(), bookingTimesDto);
        assertEquals(1, bookingDto.getBody().getId());
    }

    @Test
    void approveTest() {
        ResponseEntity<User> owner = userController.createUser(user);
        ResponseEntity<User> booker = userController.createUser(user1);
        ResponseEntity<ItemDto> item = itemController.create(itemDto, owner.getBody().getId());
        ResponseEntity<BookingDto> bookingDto = bookingController.create(booker.getBody().getId(), bookingTimesDto);
        assertEquals(WAITING, bookingDto.getBody().getStatus());
        ResponseEntity<BookingDto> approvedBooking = bookingController
                .approve(owner.getBody().getId(), bookingDto.getBody().getId(), true);
        assertEquals(APPROVED, approvedBooking.getBody().getStatus());
    }

    @Test
    void getBookingByIdTest() {
        ResponseEntity<User> owner = userController.createUser(user);
        ResponseEntity<User> booker = userController.createUser(user1);
        ResponseEntity<ItemDto> item = itemController.create(itemDto, owner.getBody().getId());
        ResponseEntity<BookingDto> bookingDto = bookingController.create(booker.getBody().getId(), bookingTimesDto);
        assertEquals(bookingDto.getBody().getId(), bookingController.getById(2, 1).getBody().getId());
    }

    @Test
    void getAllBookingsByUserTest() {
        ResponseEntity<User> owner = userController.createUser(user);
        ResponseEntity<User> booker = userController.createUser(user1);
        ResponseEntity<ItemDto> item = itemController.create(itemDto, owner.getBody().getId());
        ResponseEntity<BookingDto> bookingDto = bookingController.create(booker.getBody().getId(), bookingTimesDto);
        assertEquals(1, bookingController.getAllByUser(2,"WAITING", 0,2).getBody().size());
    }

    @Test
    void getAllBookingsByOwnerTest() {
        ResponseEntity<User> owner = userController.createUser(user);
        ResponseEntity<User> booker = userController.createUser(user1);
        ResponseEntity<ItemDto> item = itemController.create(itemDto, owner.getBody().getId());
        ResponseEntity<BookingDto> bookingDto = bookingController.create(booker.getBody().getId(), bookingTimesDto);
        assertEquals(1, bookingController.getAllByOwner(1,"WAITING", 0,2).getBody().size());
    }

    @Test
    void notFoundExcWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingController.create(1,bookingTimesDto));
        assertThrows(NotFoundException.class, () -> bookingController.getAllByOwner(1,"ALL", 1,1));
        assertThrows(NotFoundException.class, () -> bookingController.getAllByUser(1,"ALL", 1,1));
        assertThrows(NotFoundException.class, () -> bookingController.create(1,bookingTimesDto));
    }

    @Test
    void badRequestExcNegativeSize() {
        userController.createUser(user);

        assertThrows(BadRequestException.class, () -> bookingController.getAllByUser(1,"ALL",1,-1));
        assertThrows(BadRequestException.class, () -> bookingController.getAllByOwner(1,"ALL",1,-1));
    }

    @Test
    void badRequestExcWhenItemIsNotAvailable() {
        userController.createUser(user);
        userController.createUser(user1);
        itemDto.setAvailable(false);
        itemController.create(itemDto,1);

        assertThrows(BadRequestException.class, () -> bookingController.create(2,bookingTimesDto));
    }

    @Test
    void notFoundExceptionWhenBookerIsOwner() {
        userController.createUser(user);
        itemController.create(itemDto,1);

        assertThrows(NotFoundException.class, () -> bookingController.create(1,bookingTimesDto));
    }

    @Test
    void badRequestExcWhenWrongEnd() {
        userController.createUser(user);
        userController.createUser(user1);
        itemController.create(itemDto,1);
        bookingTimesDto.setEnd(LocalDateTime.of(1999,12,13,22,00));

        assertThrows(BadRequestException.class, () -> bookingController.create(2,bookingTimesDto));
    }

    @Test
    void badRequestExcApproveWhenStatusIsCanceled() {
        userController.createUser(user);
        userController.createUser(user1);
        itemController.create(itemDto,1);
        ResponseEntity<BookingDto> bookingDto = bookingController.create(2,bookingTimesDto);
        bookingController.approve(1,1,false);

        assertThrows(BadRequestException.class, () -> bookingController.approve(1,1,true));
    }

    @Test
    void notFoundExceptionWhenUserIsNotOwner() {
        userController.createUser(user);
        userController.createUser(user1);
        itemController.create(itemDto,1);
        ResponseEntity<BookingDto> bookingDto = bookingController.create(2,bookingTimesDto);

        assertThrows(NotFoundException.class, () -> bookingController.approve(2,1,true));
    }

    @Test
    void notFoundExceptionWhenBookingIdIsWrong() {
        userController.createUser(user);
        userController.createUser(user1);
        itemController.create(itemDto,1);
        ResponseEntity<BookingDto> bookingDto = bookingController.create(2,bookingTimesDto);

        assertThrows(NotFoundException.class, () -> bookingController.approve(1,2,true));
        assertThrows(NotFoundException.class, () -> bookingController.getById(1,2));
    }

    @Test
    void notFoundExceptionWhenUserIsNotBookerOrOwner() {
        userController.createUser(user);
        userController.createUser(user1);
        userController.createUser(User.builder().name("wrong").email("wrong@mail").build());
        itemController.create(itemDto,1);
        ResponseEntity<BookingDto> bookingDto = bookingController.create(2,bookingTimesDto);

        assertThrows(NotFoundException.class, () -> bookingController.getById(3,1));
    }

}

package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    private User user2;

    private User user3;

    private Item item;

    private Booking booking;

    private Booking booking2;

    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("A@mail.ru")
                .name("name")
                .build();

        user2 = User.builder()
                .email("2@mail.ru")
                .name("second")
                .build();

        user3 = User.builder()
                .email("3@mail.ru")
                .name("third")
                .build();

        item = Item.builder()
                .description("desc")
                .name("item")
                .owner(user3)
                .available(true)
                .build();

        booking = Booking.builder()
                .booker(user)
                .item(item)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2023,11,1,1,1,1))
                .end(LocalDateTime.of(2023,12,2,2,2,2))
                .build();

        booking2 = Booking.builder()
                .booker(user2)
                .item(item)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2023,5,1,1,1,1))
                .end(LocalDateTime.of(2023,6,2,2,2,2))
                .build();

        endDate = LocalDateTime.of(2024,01,01,1,1,1);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);
    }

    @Test
    void findAllByBookerIdOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdOrderByStartDesc(2, PageRequest.of(0,10)).toList();

        assertEquals(user2.getName(), list.get(0).getBooker().getName());
        assertEquals(booking2, list.get(0));
    }

    @Test
    void findAllByItemOwnerIdAndAndEndBeforeOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemOwnerIdAndAndEndBeforeOrderByStartDesc(3,endDate,PageRequest.of(0,10)).toList();

        assertEquals(user3, list.get(0).getItem().getOwner());
    }

    @Test
    void findAllByItemIdAndStartIsBeforeOrderByEndDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemIdAndStartIsBeforeOrderByEndDesc(1,endDate);

        assertEquals(item, list.get(0).getItem());
    }

    @Test
    void findAllByItemIdAndStartIsAfterOrderByStartAscTest() {
        List<Booking> list = bookingRepository
                .findAllByItemIdAndStartIsAfterOrderByStartAsc(1,LocalDateTime.now());

        assertEquals(item, list.get(0).getItem());
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(1,endDate,PageRequest.of(0,10)).toList();

        assertEquals(user, list.get(0).getBooker());
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(3,PageRequest.of(0,10)).toList();

        assertEquals(user3, list.get(0).getItem().getOwner());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(1, Status.WAITING,PageRequest.of(0,10)).toList();

        assertEquals(1,list.size());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescTestList() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(1, Status.WAITING);

        assertEquals(1,list.size());
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndStartAfterOrderByStartDesc(1, LocalDateTime.now(),PageRequest.of(0,10)).toList();

        assertEquals(1,list.size());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(1,
                        LocalDateTime.of(2023,11,2,1,1,1),
                        LocalDateTime.of(2023,12,2,1,2,2),
                        PageRequest.of(0,10)).toList();

        assertEquals(1,list.size());
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(3,Status.WAITING,PageRequest.of(0,10)).toList();

        assertEquals(2,list.size());
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(3,LocalDateTime.now(),PageRequest.of(0,10)).toList();

        assertEquals(2,list.size());
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        List<Booking> list = bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(3,
                        LocalDateTime.of(2023,11,2,1,1,1),
                        LocalDateTime.of(2023,12,2,1,2,2),
                        PageRequest.of(0,10)).toList();

        assertEquals(1,list.size());
    }

    @Test
    void findAllByBookerIdAndItemIdAndStatusAndEndBeforeTest() {
        List<Booking> list = bookingRepository
                .findAllByBookerIdAndItemIdAndStatusAndEndBefore(1,1,Status.WAITING,endDate);

        assertEquals(1,list.size());
    }
}

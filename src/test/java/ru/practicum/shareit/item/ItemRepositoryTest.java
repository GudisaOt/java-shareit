package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item;

    private User user;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("A@mail.ru")
                .name("name")
                .build();

        item = Item.builder()
                .description("desc")
                .name("item")
                .available(true)
                .build();

        itemRequest = ItemRequest.builder()
                .description("desc")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void findAllByOwnerIdTest() {
        User owner = userRepository.save(user);
        item.setOwner(owner);
        itemRepository.save(item);

        assertEquals(item.getName(), itemRepository.findAllByOwnerId(1).get(0).getName());
    }

    @Test
    void findAllByRequestIdTest() {
        User owner = userRepository.save(user);
        item.setRequest(itemRequestRepository.save(itemRequest));
        item.setOwner(owner);
        itemRepository.save(item);

        assertEquals(item.getName(), itemRepository.findAllByRequestId(1).get(0).getName());
    }


}

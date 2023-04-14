package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    UserRepository userRepository;

    private User user;

    private User secondUser;

    private ItemRequest itemRequest1;

    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("A@mail.ru")
                .name("name")
                .build();

        secondUser = User.builder()
                .email("2@mail.ru")
                .name("second")
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("desc")
                .created(LocalDateTime.now())
                .build();

        itemRequest2 = ItemRequest.builder()
                .description("future")
                .created(LocalDateTime.of(2024,11,11,11,11))
                .build();
    }

    @Test
    void findAllByRequestorIdOrderByCreatedAscTest() {
        User requestor = userRepository.save(user);
        itemRequest1.setRequestor(requestor);
        itemRequest2.setRequestor(requestor);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        List<ItemRequest> list = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(requestor.getId());

        assertEquals(itemRequest2.getDescription(), list.get(1).getDescription());
    }

    @Test
    void findAllByRequestorNotLikeOrderByCreatedAscTest() {
        User requestor = userRepository.save(user);
        User user1 = userRepository.save(secondUser);
        itemRequest1.setRequestor(requestor);
        itemRequest2.setRequestor(requestor);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        Page<ItemRequest> list = itemRequestRepository
                .findAllByRequestorNotLikeOrderByCreatedAsc(user1, PageRequest.of(0,10));

        assertEquals(itemRequest1.getDescription(), list.get().collect(Collectors.toList()).get(0).getDescription());
    }
}

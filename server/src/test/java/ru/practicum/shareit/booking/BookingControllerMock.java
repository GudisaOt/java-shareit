package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerMock {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemMapper itemMapper;

    private ItemDto itemDto;

    private User user;

    private User user1;

    private BookingTimesDto bookingTimesDto;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.ru")
                .build();

        user1 = User.builder()
                .id(2)
                .name("booker")
                .email("booker@mail.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("desc")
                .available(true)
                .build();

        bookingTimesDto = BookingTimesDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2023,7, 17, 22, 00))
                .end(LocalDateTime.of(2023, 8, 15, 20, 00))
                .build();

        bookingDto = BookingDto.builder()
                .id(1)
                .booker(user1)
                .item(itemMapper.toItem(itemDto))
                .start(LocalDateTime.of(2023, 7, 17,22,00))
                .end(LocalDateTime.of(2023,8,15,20,00))
                .build();
    }

    @Test
    void createBookingMockTest() throws Exception {
        when(bookingService.add(any(), anyInt()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingTimesDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void approveBookingMockTest() throws Exception {
        bookingDto.setStatus(Status.APPROVED);
        when(bookingService.approve(anyInt(),anyInt(),anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getBookingByIdMockTest() throws Exception {
        when(bookingService.getById(anyInt(),anyInt()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getAllByUserMockTest() throws Exception {
        when(bookingService.getAllByUser(anyInt(),anyString(),anyInt(),anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getAllByOwnerMockTest() throws Exception {
        when(bookingService.getAllByOwner(anyInt(),anyString(),anyInt(),anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }
}

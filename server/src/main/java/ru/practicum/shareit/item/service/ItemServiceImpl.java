package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> getAll(int userId, int from, int size) {
        List<ItemDto> items = itemRepository.findAllByOwnerIdOrderByIdAsc(userId, PageRequest.of(from,size))
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        items.forEach(itemDto -> {
            itemDto.setLastBooking(bookingRepository
                    .findAllByItemIdAndStartIsBeforeOrderByEndDesc(itemDto.getId(), LocalDateTime.now()).isEmpty() ?
                    null : bookingMapper.toBookingTimesDto(bookingRepository
                    .findAllByItemIdAndStartIsBeforeOrderByEndDesc(itemDto.getId(), LocalDateTime.now()).get(0)));
            itemDto.setNextBooking(bookingRepository
                    .findAllByItemIdAndStartIsAfterOrderByStartAsc(itemDto.getId(), LocalDateTime.now()).isEmpty() ?
                    null : bookingMapper.toBookingTimesDto(bookingRepository
                    .findAllByItemIdAndStartIsAfterOrderByStartAsc(itemDto.getId(), LocalDateTime.now()).get(0)));
            itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId())
                    .stream().map(commentMapper::toCommentDto).collect(Collectors.toList()));
        });
        return items;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getById(int id, int userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("item not found"));
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (userId == item.getOwner().getId()) {
            List<Booking> nextList = bookingRepository.findAllByItemIdAndStartIsAfterOrderByStartAsc(id, LocalDateTime.now());
            List<BookingTimesDto> nextDtos = nextList
                    .stream()
                    .filter(s -> s.getStatus().equals(Status.APPROVED))
                    .map(bookingMapper::toBookingTimesDto)
                    .collect(Collectors.toList());

            BookingTimesDto next = nextDtos
                    .stream()
                    .findFirst()
                    .orElse(null);

            List<Booking> lastList = bookingRepository.findAllByItemIdAndStartIsBeforeOrderByEndDesc(id, LocalDateTime.now());
            List<BookingTimesDto> lastDtos = lastList
                    .stream()
                    .filter(s -> s.getStatus().equals(Status.APPROVED))
                    .map(bookingMapper::toBookingTimesDto)
                    .collect(Collectors.toList());

            BookingTimesDto last = lastDtos
                    .stream()
                    .findFirst()
                    .orElse(null);
            itemDto.setNextBooking(next);
            itemDto.setLastBooking(last);
        }
        itemDto.setComments(commentRepository.findAllByItemId(id)
                .stream().map(commentMapper::toCommentDto).collect(Collectors.toList()));

        return itemDto;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userMapper.toUser(userService.getById(userId)));
        if (itemDto.getRequestId() > 0) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                            .orElseThrow(() -> new NotFoundException("Request not found!"));
            item.setRequest(itemRequest);
        }

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(Item item, int id, int userId) {
        Item itemToUpd = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        if (itemToUpd.getOwner().getId() != userId) {
            throw new NotFoundException("Wrong user Id");
        }
        Optional.ofNullable(item.getName()).ifPresent(itemToUpd::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(itemToUpd::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(itemToUpd::setAvailable);
        return itemMapper.toItemDto(itemRepository.save(itemToUpd));
    }

    @Transactional
    @Override
    public void delete(int id) {
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        if (text.isBlank()) {
            return itemDtos;
        }
        for (Item item : itemRepository.findAll()) {
            if (isFound(item, text)) {
                itemDtos.add(itemMapper.toItemDto(item));
            }
        }
        return itemDtos;
    }

    private Boolean isFound(Item item, String text) {
        return (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
               item.getName().toLowerCase().contains(text.toLowerCase())) && item.getAvailable();
    }

    @Transactional
    @Override
    public CommentDto postComment(Comment comment, int authorId, int itemId) {
        User user = userMapper.toUser(userService.getById(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
        if (comment.getText().isBlank()) {
            throw new BadRequestException("Text is blank!");
        }
        if (bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(authorId, Status.APPROVED).isEmpty()) {
            throw new BadRequestException("This user has not booked an item");
        }

        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndItemIdAndStatusAndEndBefore(authorId, itemId, Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new BadRequestException("Bad request!");
        }
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}

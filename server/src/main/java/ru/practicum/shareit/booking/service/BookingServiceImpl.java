package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingTimesDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllByOwner(int ownerId, String state, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Bad request");
        }
        PageRequest pageRequest = PageRequest.of(from, size);
        User user = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found!"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId, pageRequest).toList());
                break;
            case "WAITING":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.WAITING, pageRequest).toList());
                break;
            case "REJECTED":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.REJECTED, pageRequest).toList());
                break;
            case "PAST":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdAndAndEndBeforeOrderByStartDesc(ownerId,
                        LocalDateTime.now(), pageRequest).toList());
                break;
            case "CURRENT":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(),LocalDateTime.now(), pageRequest).toList());
                break;
            case "FUTURE":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now(),
                                pageRequest).toList());
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllByUser(int userId, String state, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("BAd request");
        }
        PageRequest pageRequest = PageRequest.of(from, size);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        ArrayList<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                Slice<Booking> bookingSlice = bookingRepository.findAllByBookerIdOrderByStartDesc(userId,pageRequest);
                while (!bookingSlice.hasContent() && bookingSlice.getNumber() > 0) {
                    bookingSlice = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, PageRequest.of(bookingSlice.getNumber() - 1, bookingSlice.getSize()));
                }
                for (Booking b: bookingSlice) {
                    bookings.add(b);
                }
                break;
            case "WAITING":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING,
                        pageRequest).toList());
                break;
            case "REJECTED":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED,
                        pageRequest).toList());
                break;
            case "PAST":
                bookings.addAll(bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageRequest).toList());
                break;
            case "CURRENT":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest).toList());
                break;
            case "FUTURE":
                bookings.addAll(bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageRequest).toList());
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookingDto add(BookingTimesDto bookingTimesDto, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingTimesDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (item.getOwner().getId() == user.getId()) {
            throw new NotFoundException("Error");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }

        Booking booking = bookingMapper.toBooking(bookingTimesDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approve(int bookingId, int userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("You are not the owner!");
        }
        if (booking.getStatus().equals(Status.WAITING)) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BadRequestException();
        } else {
            throw new NotFoundException("Booking already rejected or approved");
        }
        bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("Booking is available members only!");
        }
        return bookingMapper.toBookingDto(booking);
    }
}

package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public List<BookingDto> getAllByOwner(int ownerId, String state) {
        User user = userRepository.findById(ownerId).orElseThrow(()-> new NotFoundException("User not found!"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId));
                break;
            case "WAITING":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.WAITING));
                break;
            case "REJECTED":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.REJECTED));
                break;
            case "PAST":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdAndAndEndBeforeOrderByStartDesc(ownerId,
                        LocalDateTime.now()));
                break;
            case "CURRENT":
                bookings.addAll(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(),LocalDateTime.now()));
                break;
            case "FUTURE":
                bookings.addAll(bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now()));
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingDto> getAllByUser(int userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found!"));
        ArrayList<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings.addAll(bookingRepository.findAllByBookerIdOrderByStartDesc(userId));
                break;
            case "WAITING":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING));
                break;
            case "REJECTED":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED));
                break;
            case "PAST":
                bookings.addAll(bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case "CURRENT":
                bookings.addAll(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now()));
                break;
            case "FUTURE":
                bookings.addAll(bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookingDto add(BookingTimesDto bookingTimesDto, int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bookingTimesDto.getItemId())
                .orElseThrow(()-> new NotFoundException("Item not found"));
        if (item.getOwner().getId() == user.getId()) {
            throw new NotFoundException("Error");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        if (dateValidator(bookingTimesDto)) {
            throw new BadRequestException("Booking dates are incorrect");
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
                .orElseThrow(()-> new NotFoundException("Booking not found"));
        if(userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("You are not hte owner!");
        }
        if (booking.getStatus().equals(Status.WAITING)) {
            if (approved){
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BadRequestException();
        } else {
            throw new NotFoundException("Booking already rejected or approved");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto getById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId).
                orElseThrow(()-> new NotFoundException("Booking not found"));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("Booking is available members only!");
        }
        return bookingMapper.toBookingDto(booking);
    }

    private Boolean dateValidator (BookingTimesDto bookingTimesDto) {
        return (bookingTimesDto.getEnd().isBefore(bookingTimesDto.getStart())
                || bookingTimesDto.getStart().equals(bookingTimesDto.getEnd()));
    }
}

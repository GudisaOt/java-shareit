package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;


import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId,
                                                                             LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int ownerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId,
                                                                                LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime end);

    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(int itemId, LocalDateTime now);

    List<Booking> findAllByItemIdAndStartIsBeforeOrderByEndDesc(int itemId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(int userId, int itemId,
                                                                  Status status, LocalDateTime now);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Slice<Booking> findAllByBookerIdOrderByStartDesc(int bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookreId, Status status);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId,
                                                                             LocalDateTime start, LocalDateTime end,
                                                                             Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int ownerId, Status status, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId,
                                                                                LocalDateTime start, LocalDateTime end,
                                                                                Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(int itemId, LocalDateTime now);

    List<Booking> findAllByItemIdAndStartIsBeforeOrderByEndDesc(int itemId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(int userId, int itemId,
                                                                  Status status, LocalDateTime now);

}

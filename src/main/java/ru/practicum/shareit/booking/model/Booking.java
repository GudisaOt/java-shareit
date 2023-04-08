package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@Builder
public class Booking implements Comparable<Booking> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date", nullable = false)
    @JsonProperty(value = "start")
    @NotNull
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    @JsonProperty(value = "end")
    @NotNull
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id", nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Override
    public boolean equals(Object o) {
        return ((Booking) o).getStart().equals(getStart());
    }


    @Override
    public int compareTo(Booking o) {
        return getStart().compareTo(o.getStart());
    }
}

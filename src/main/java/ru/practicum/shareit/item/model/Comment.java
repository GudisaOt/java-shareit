package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "comments")
public class Comment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User user;
    @Column(name = "created")
    private LocalDateTime created;
}

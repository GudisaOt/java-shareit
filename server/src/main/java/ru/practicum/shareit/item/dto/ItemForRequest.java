package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemForRequest {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private int requestId;
    private Boolean available;

}

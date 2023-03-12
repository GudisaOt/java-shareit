package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    
    @Builder
    public ItemDto(int id, String name, String description, boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}

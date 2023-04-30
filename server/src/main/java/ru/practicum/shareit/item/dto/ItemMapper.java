package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    @Mapping(target = "ownerId", source = "item.owner.id")
    @Mapping(target = "requestId", source = "item.request.id")
    ItemForRequest toItemForRequest(Item item);
}

package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.ItemRequest;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    ItemRequestDto toRequestDto(ItemRequest itemRequest);

    ItemRequest toRequest(ItemRequestDto itemRequestDto);
}

package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.ItemRequest;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    //@Mapping(target = "items", source = "new ArrayList<>()")
    ItemRequestDto toRequestDto(ItemRequest itemRequest);

    ItemRequest toRequest(ItemRequestDto itemRequestDto);
}

package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    ItemRequestDto findById(int userId, int requestId);

    List<ItemRequestDto>  getAll(Integer userId, int from, int size);

    List<ItemRequestDto> getAllByUser(int userId);
}

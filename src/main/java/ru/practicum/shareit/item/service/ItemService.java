package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemDto> getAll(int userId);

    ItemDto getById(int id);

    ItemDto create(Item item, int userId);

    ItemDto update(Item item,int id, int userId);

    void delete(int id);

    List<ItemDto> search(String text);
}

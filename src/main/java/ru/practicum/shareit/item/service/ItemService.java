package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemDto> getAll(int userId);

    ItemDto getById(int id, int userId);

    ItemDto create(ItemDto item, int userId);

    ItemDto update(Item item,int id, int userId);

    void delete(int id);

    List<ItemDto> search(String text);

    CommentDto postComment(Comment comment, int authorId, int itemId);
}

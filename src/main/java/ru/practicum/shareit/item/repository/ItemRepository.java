package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository {
    Collection<Item> getAll ();
    Item getById (int id);
    Item create (Item item);
    Item update (Item item, int id);
    void delete (int id);
    List<Item> search (String text);
}

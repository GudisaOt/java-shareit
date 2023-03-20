package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private HashMap<Integer, Item> items = new HashMap<>();
    private static int genId = 1;

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }

    @Override
    public Item getById(int id) {
        try {
            return items.get(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public Item create(Item item) {
        item.setId(genId);
        genId++;
        items.put(item.getId(), item);
        return getById(item.getId());
    }

    @Override
    public Item update(Item item, int id) {
        items.put(item.getId(), item);
        return getById(item.getId());
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }

    @Override
    public List<Item> search(String text) {
        String textToLower = text.toLowerCase();
        List<Item> foundItems = new ArrayList<>();
        if (text.isBlank()) {
            return foundItems;
        }
        for (Item item:items.values()) {
            if ((item.getName().toLowerCase().contains(textToLower) || item.getDescription().toLowerCase().contains(textToLower)) && item.getAvailable()) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }
}

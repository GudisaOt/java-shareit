package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }


    @Override
    public Collection<ItemDto> getAll(int userId) {
        List<ItemDto> userItems = new ArrayList<>();
        for (Item item:itemRepository.getAll()) {
            if (item.getOwner().getId() == userId) {
                userItems.add(toItemDto(item));
            }
        }
        return userItems;
    }

    @Override
    public ItemDto getById(int id) {
        return toItemDto(itemRepository.getById(id));
    }

    @Override
    public ItemDto create(Item item, int userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        item.setOwner(user);
        return toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(Item item, int id, int userId) {
        Item itemToUpd = itemRepository.getById(id);
        if (itemToUpd.getOwner().getId() != userId) {
            throw new NotFoundException("Предмет отсутсвует у пользователя");
        }
        if (item.getName() != null) {
            itemToUpd.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToUpd.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemToUpd.setAvailable(item.getAvailable());
        }
        return toItemDto(itemRepository.update(itemToUpd, id));
    }

    @Override
    public void delete(int id) {
        getById(id);
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item: itemRepository.search(text)) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }
}

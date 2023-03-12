package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String HEADER = "X-Sharer-User-Id";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> getAll (@RequestHeader(HEADER) int userId){
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById (@PathVariable int id){
        return itemService.getById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search (@RequestParam String text){
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto create (@Valid @RequestBody Item item, @RequestHeader(HEADER) int userId){
        return itemService.create(item,userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update (@RequestBody Item item, @PathVariable int id, @RequestHeader(HEADER) int userId){
       return itemService.update(item,id,userId);
    }
}

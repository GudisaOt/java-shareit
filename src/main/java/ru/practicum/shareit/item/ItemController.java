package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> getAll(@RequestHeader(header) Integer userId) {
        return ResponseEntity.ok().body(itemService.getAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Integer id, @RequestHeader(header) Integer userId) {
        return ResponseEntity.ok().body(itemService.getById(id, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        return ResponseEntity.ok().body(itemService.search(text));
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto item, @RequestHeader(header) Integer userId) {
        return ResponseEntity.ok().body(itemService.create(item,userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestBody Item item, @PathVariable Integer id, @RequestHeader(header) Integer userId) {
       return ResponseEntity.ok().body(itemService.update(item,id,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        itemService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> postComment(@PathVariable Integer itemId, @RequestHeader(header) Integer userId,
                                  @RequestBody Comment comment) {
        return ResponseEntity.ok(itemService.postComment(comment, userId, itemId));
    }
}

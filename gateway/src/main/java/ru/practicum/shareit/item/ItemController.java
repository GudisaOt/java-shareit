package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentGateDto;
import ru.practicum.shareit.item.dto.ItemGateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private final String header = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(header) Long userId,
                                         @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam (name = "size", defaultValue = "10") int size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, @RequestHeader(header) Long userId) {
        return itemClient.getItem(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam (name = "size", defaultValue = "10") int size) {
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemGateDto item, @RequestHeader(header) Long userId) {
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemGateDto item,
                                         @PathVariable Long id, @RequestHeader(header) Long userId) {
        return itemClient.updateItem(item, id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return itemClient.deleteItem(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable Long itemId, @RequestHeader(header) Long userId,
                                              @Valid @RequestBody CommentGateDto comment) {
        return itemClient.createComment(itemId, userId, comment);
    }
}

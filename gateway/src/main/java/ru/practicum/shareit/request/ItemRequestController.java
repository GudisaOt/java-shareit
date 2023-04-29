package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId, @RequestHeader(header) Long userId) {
        return itemRequestClient.getItemRequestById(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(header) Long userId) {
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId,
                                         @Valid @RequestBody RequestDto requestDto) {
        return itemRequestClient.createItemRequest(userId, requestDto);
    }
}

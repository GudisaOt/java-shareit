package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    private final String header = "X-Sharer-User-Id";

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getById(@PathVariable int requestId, @RequestHeader(header) int userId) {
        return ResponseEntity.ok(itemRequestService.findById(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllByUser(@RequestHeader(header) int userId) {
        return ResponseEntity.ok(itemRequestService.getAllByUser(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAll(@RequestHeader(header) int userId, @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(itemRequestService.getAll(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(@RequestHeader(header) int userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok(itemRequestService.create(userId, itemRequestDto));
    }
}

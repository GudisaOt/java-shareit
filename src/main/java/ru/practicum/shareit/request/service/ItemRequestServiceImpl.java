package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final RequestMapper requestMapper;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemRequestDto create(int userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        ItemRequest itemRequest = requestMapper.toRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return requestMapper.toRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto findById(int userId, int requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found!"));
        ItemRequestDto itemRequestDto = requestMapper.toRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequestId(requestId)
                .stream()
                .map(itemMapper::toItemForRequest)
                .collect(Collectors.toList()));
        return itemRequestDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getAll(int userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Bad request");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        List<ItemRequestDto> itemRequestDtos = itemRequestRepository
                .findAllByRequestorNotLikeOrderByCreatedAsc(user, PageRequest.of(from, size))
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
        for (ItemRequestDto request: itemRequestDtos) {
            request.setItems(itemRepository.findAllByRequestId(request.getId())
                    .stream()
                    .map(itemMapper::toItemForRequest)
                    .collect(Collectors.toList()));;
        }
        return itemRequestDtos;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getAllByUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        List<ItemRequestDto> itemRequestDtos = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(userId)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
        for (ItemRequestDto request : itemRequestDtos) {
            request.setItems(itemRepository.findAllByRequestId(request.getId())
                    .stream()
                    .map(itemMapper::toItemForRequest)
                    .collect(Collectors.toList()));
        }
        return itemRequestDtos;
    }
}

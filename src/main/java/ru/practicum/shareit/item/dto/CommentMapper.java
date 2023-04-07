package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto (Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }
}

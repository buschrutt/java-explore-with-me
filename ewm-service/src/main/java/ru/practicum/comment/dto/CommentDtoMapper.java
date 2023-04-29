package ru.practicum.comment.dto;

import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;

public class CommentDtoMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEventId())
                .commentatorId(comment.getCommentatorId())
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }

    public static CommentWithNamesDto toCommentWithNamesDto(Comment comment, String event, String commentator) {
        return CommentWithNamesDto.builder()
                .id(comment.getId())
                .event(event)
                .commentator(commentator)
                .text(comment.getText())
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, String status) {
        return Comment.builder()
                .eventId(commentDto.getEventId())
                .commentatorId(commentDto.getCommentatorId())
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .status(status)
                .build();
    }
}

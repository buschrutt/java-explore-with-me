package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentWithNamesDto;
import ru.practicum.error.EwmException;

import java.util.List;

public interface CommentService {
    CommentWithNamesDto findCommentById(Integer commentId) throws EwmException;

    List<CommentWithNamesDto> findCommentsByEventId(Integer eventId, Integer from, Integer size);

    CommentDto addComment(CommentDto commentDto);

    CommentDto updateComment(Integer commentId, Integer userId, CommentDto commentDto) throws EwmException;

    CommentDto updateCommentStatus(Integer commentId, CommentDto commentDto) throws EwmException;

    void deleteCommentByUser(Integer userId, Integer commentId) throws EwmException;

    void deleteCommentByAdmin(Integer commentId);
}

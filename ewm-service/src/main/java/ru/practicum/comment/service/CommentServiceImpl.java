package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentDtoMapper;
import ru.practicum.comment.dto.CommentWithNamesDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.error.EwmException;
import ru.practicum.error.model.EwmExceptionModel;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public CommentWithNamesDto findCommentById(Integer commentId) throws EwmException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Comment id:" + commentId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        String event = eventRepository.findById(comment.getEventId()).orElseThrow().getTitle();
        String commentator = userRepository.findById(comment.getCommentatorId()).orElseThrow().getName();
        return CommentDtoMapper.toCommentWithNamesDto(comment, event, commentator);
    }

    @Override
    public List<CommentWithNamesDto> findCommentsByEventId(Integer eventId, Integer from, Integer size) throws EwmException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        String title = event.getTitle();
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> eventComments = commentRepository.findCommentsByEventIdAndStatusOrderByCreatedDesc(eventId, "PUBLISHED", pageable);
        List<CommentWithNamesDto> eventCommentWithNamesDtoList = new ArrayList<>();
        for (Comment comment : eventComments) {
            String commentator = userRepository.findById(comment.getCommentatorId()).orElseThrow().getName();
            eventCommentWithNamesDtoList.add(CommentDtoMapper.toCommentWithNamesDto(comment, title, commentator));
        }
        return eventCommentWithNamesDtoList;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        Comment comment = CommentDtoMapper.toComment(commentDto, "PENDING");
        return CommentDtoMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Integer commentId, Integer userId, CommentDto commentDto) throws EwmException {
        Comment comment = ValidateUpdateComment(commentDto, commentId);
        Comment newComment = CommentDtoMapper.toComment(commentDto, comment.getStatus());
        if (newComment.getText() != null) {
            comment.setText(newComment.getText());
        } else {
            throw new EwmException(new EwmExceptionModel("Field: text. Error: must not be blank. Value: null", "Incorrectly made request.", "BAD_REQUEST",
                    HttpStatus.BAD_REQUEST));
        }
        return CommentDtoMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateCommentStatus(Integer commentId, CommentDto commentDto) throws EwmException {
        Comment comment = ValidateUpdateComment(commentDto, commentId);
        Comment newComment = CommentDtoMapper.toComment(commentDto, commentDto.getStatus());
        if (newComment.getStatus() != null) {
            comment.setStatus(newComment.getStatus());
        } else {
            throw new EwmException(new EwmExceptionModel("Field: status. Error: must not be blank. Value: null", "Incorrectly made request.", "BAD_REQUEST",
                    HttpStatus.BAD_REQUEST));
        }
        return CommentDtoMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Integer userId, Integer commentId) throws EwmException {
        if (!Objects.equals(commentRepository.findById(commentId).orElseThrow().getCommentatorId(), userId)) {
            throw new EwmException(new EwmExceptionModel("User is not the Commentator", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
        commentRepository.delete(commentRepository.findById(commentId).orElseThrow());
    }

    @Override
    public void deleteCommentByAdmin(Integer commentId) {
        commentRepository.delete(commentRepository.findById(commentId).orElseThrow());
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    Comment ValidateUpdateComment(CommentDto commentDto, Integer commentId) throws EwmException {
        Comment comment;
        if (commentDto.getId() == null) {
            throw new EwmException(new EwmExceptionModel("Field: id. Error: must not be blank. Value: null", "Incorrectly made request.", "BAD_REQUEST",
                    HttpStatus.BAD_REQUEST));
        }
        comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Comment id:" + commentId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        return comment;
    }
}

package ru.practicum.comment;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentWithNamesDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.error.EwmException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.Constants.HEADER;

@RestController
@AllArgsConstructor
@RequestMapping
public class CommentController {

    CommentService commentService;

    @GetMapping("/admin/comments/{commentId}") // ++
    public CommentWithNamesDto findCommentById(@PathVariable Integer commentId) throws EwmException {
        return commentService.findCommentById(commentId);
    }

    @GetMapping("/comments/{eventId}")
    public List<CommentWithNamesDto> findCommentsByEventId(@PathVariable Integer eventId,
                                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return commentService.findCommentsByEventId(eventId, from, size);
    }

    @PostMapping("/comments") // ++
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }

    @PatchMapping("/comments/{commentId}") // ++
    public CommentDto updateComment(@PathVariable Integer commentId,
                                    @RequestHeader(value = HEADER) Integer userId,
                                    @RequestBody CommentDto commentDto) throws EwmException {
        return commentService.updateComment(commentId, userId, commentDto);
    }

    @PatchMapping("/admin/comments/{commentId}") // ++
    public CommentDto updateCommentStatus(@PathVariable Integer commentId,
                                          @RequestBody CommentDto commentDto) throws EwmException {
        return commentService.updateCommentStatus(commentId, commentDto);
    }

    @DeleteMapping("comments/{commentId}") // ++
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@RequestHeader(value = HEADER) Integer userId,
                                    @PathVariable Integer commentId) throws EwmException {
        commentService.deleteCommentByUser(userId, commentId);
    }

    @DeleteMapping("/admin/comments/{commentId}") // ++
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Integer commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}

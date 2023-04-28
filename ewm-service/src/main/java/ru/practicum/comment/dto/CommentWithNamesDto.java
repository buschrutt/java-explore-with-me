package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@PackagePrivate
public class CommentWithNamesDto {
    Integer id;
    String event;
    String commentator;
    String text;
    LocalDateTime created;
    String status;
}

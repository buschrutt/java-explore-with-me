package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@PackagePrivate
public class CommentDto {
    Integer id;
    @NotNull
    Integer eventId;
    @NotNull
    Integer commentatorId;
    @NotNull
    String text;
    LocalDateTime created;
    String status;
}

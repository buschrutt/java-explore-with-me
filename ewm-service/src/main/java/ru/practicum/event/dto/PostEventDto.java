package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@PackagePrivate
@NoArgsConstructor
@AllArgsConstructor
public class PostEventDto {
    @NotNull
    String annotation;
    @NotNull
    Integer category;
    @NotNull
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    @NotNull
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    @NotNull
    String title;
}
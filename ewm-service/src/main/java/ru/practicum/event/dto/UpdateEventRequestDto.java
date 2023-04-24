package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.time.LocalDateTime;

@Data
@Builder
@PackagePrivate
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestDto {
    String annotation;
    Integer category;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    String title;
}

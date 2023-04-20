package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@PackagePrivate
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    Integer id;
    String annotation;
    Integer category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserDto initiator;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Integer views;
}

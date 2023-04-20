package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@PackagePrivate
public class RequestDto {
    Integer id;
    Integer requester;
    Integer event;
    LocalDateTime created;
    String status;
}

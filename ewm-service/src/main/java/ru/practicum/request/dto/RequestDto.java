package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    String status;
}

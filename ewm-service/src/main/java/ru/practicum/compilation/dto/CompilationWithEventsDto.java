package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;
import ru.practicum.event.model.Event;

import java.util.List;

@Data
@Builder
@PackagePrivate
public class CompilationWithEventsDto {
    Integer id;
    List<Event> events;
    Boolean pinned;
    String title;
}

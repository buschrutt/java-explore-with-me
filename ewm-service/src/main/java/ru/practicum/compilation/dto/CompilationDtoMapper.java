package ru.practicum.compilation.dto;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationEvent;
import ru.practicum.event.model.Event;

import java.util.List;

public class CompilationDtoMapper {
    public static Compilation toCompilation(CompilationDto compilationDto) {
        return Compilation.builder()
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static CompilationEvent toCompilationEvent(Integer eventId, Integer compilationId) {
        return CompilationEvent.builder()
                .eventId(eventId)
                .compilationId(compilationId)
                .build();
    }

    public static CompilationWithEventsDto toCompilationWithEventsDto(Compilation compilation, List<Event> events) {
        return CompilationWithEventsDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}

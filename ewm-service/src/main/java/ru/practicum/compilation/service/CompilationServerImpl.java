package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoMapper;
import ru.practicum.compilation.dto.CompilationWithEventsDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationEvent;
import ru.practicum.compilation.repository.CompilationEventRepository;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.EwmException;
import ru.practicum.error.model.EwmExceptionModel;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompilationServerImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationWithEventsDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> comps = compilationRepository.findCompilationsByPinned(pinned, pageable);
        List<CompilationWithEventsDto> compilationWithEventsDtoList = new ArrayList<>();
        for (Compilation comp : comps) {
            List<CompilationEvent> compEvents = compilationEventRepository.findCompilationEventsByCompilationId(comp.getId());
            List<Integer> eventIds = compEvents.stream().map(CompilationEvent::getEventId).collect(Collectors.toList());
            List<Event> events = eventRepository.findAllByIdIn(eventIds);
            compilationWithEventsDtoList.add(CompilationDtoMapper.toCompilationWithEventsDto(comp, events));
        }
        return compilationWithEventsDtoList;
    }

    @Override
    public CompilationWithEventsDto findCompilationById(Integer compId) throws EwmException {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Compilation with id:" + compId + " was not found", "The required object was not found.", "NOT_FOUND", HttpStatus.NOT_FOUND)));
        List<Event> events = eventRepository.findAllByIdIn(compilationEventRepository.findCompilationEventsByCompilationId(comp.getId())
                .stream().map(CompilationEvent::getEventId).collect(Collectors.toList()));
        return CompilationDtoMapper.toCompilationWithEventsDto(comp, events);
    }


    @Override
    public CompilationWithEventsDto addCompilation(CompilationDto compilationDto) throws EwmException {
        List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        Compilation compilation = CompilationDtoMapper.toCompilation(compilationDto);
        if (compilation.getTitle().isEmpty()) {
            throw new EwmException(new EwmExceptionModel("Field: title. Error: must not be blank. Value: null", "Incorrectly made request.", "BAD_REQUEST",
                    HttpStatus.BAD_REQUEST));
        }
        compilation = compilationRepository.save(compilation);
        for (Event event : events) {
            CompilationEvent compilationEvent = CompilationEvent.builder().eventId(event.getId()).compilationId(compilation.getId()).build();
            compilationEventRepository.save(compilationEvent);
        }
        return CompilationDtoMapper.toCompilationWithEventsDto(compilation, events);
    }

    @Override
    public void deleteCompilation(Integer compId) throws EwmException {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Compilation with id:" + compId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        compilationRepository.delete(comp);
        List<CompilationEvent> compEventList = compilationEventRepository.findCompilationEventsByCompilationId(compId);
        compilationEventRepository.deleteAll(compEventList);
    }

    @Override
    public CompilationWithEventsDto updateCompilation(Integer compId, CompilationDto compilationDto) throws EwmException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Compilation with id:" + compId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        Compilation newCompilation = CompilationDtoMapper.toCompilation(compilationDto);
        compilation = updateCompilationWithNotNullFields(compilation, newCompilation);
        compilationRepository.save(compilation);
        for (Event event : events) {
            CompilationEvent compilationEvent = CompilationEvent.builder().eventId(event.getId()).compilationId(compId).build();
            compilationEventRepository.save(compilationEvent);
        }
        return CompilationDtoMapper.toCompilationWithEventsDto(compilation, events);
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    Compilation updateCompilationWithNotNullFields(Compilation compilation, Compilation newCompilation) {
        if (newCompilation.getPinned() != null) {
            compilation.setPinned(newCompilation.getPinned());
        }
        if (newCompilation.getTitle() != null) {
            compilation.setTitle(newCompilation.getTitle());
        }
        return compilation;
    }
}

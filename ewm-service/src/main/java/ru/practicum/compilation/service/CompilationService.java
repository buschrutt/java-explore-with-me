package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationWithEventsDto;
import ru.practicum.error.ewmException;

import java.util.List;

public interface CompilationService {
    List<CompilationWithEventsDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationWithEventsDto findCompilationById(Integer compId) throws ewmException;

    CompilationWithEventsDto addCompilation(CompilationDto compilationDto) throws ewmException;

    void deleteCompilation(Integer compId) throws ewmException;

    CompilationWithEventsDto updateCompilation(Integer compId, CompilationDto compilationDto) throws ewmException;
}

package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationWithEventsDto;
import ru.practicum.error.EwmException;

import java.util.List;

public interface CompilationService {
    List<CompilationWithEventsDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationWithEventsDto findCompilationById(Integer compId) throws EwmException;

    CompilationWithEventsDto addCompilation(CompilationDto compilationDto) throws EwmException;

    void deleteCompilation(Integer compId) throws EwmException;

    CompilationWithEventsDto updateCompilation(Integer compId, CompilationDto compilationDto) throws EwmException;
}

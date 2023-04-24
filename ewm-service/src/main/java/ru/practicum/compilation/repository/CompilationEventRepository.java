package ru.practicum.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.CompilationEvent;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Integer> {
    List<CompilationEvent> findCompilationEventsByCompilationId(Integer compId);
}

package ru.practicum.stats.service;

import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean isUnique);

    void postHitToStats(HitDto hitDto);
}

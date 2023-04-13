package ru.practicum.statistics.stats.service;

import ru.practicum.statistics.stats.dto.HitDto;
import ru.practicum.statistics.stats.dto.StatsDto;

import java.util.List;

public interface StatsService {
    List<StatsDto> getFrameStats(String start, String end, List<String> uris, Boolean isUnique);

    void postHitToStats(HitDto hitDto);
}

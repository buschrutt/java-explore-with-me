package ru.practicum.explore.stats.service;

import ru.practicum.explore.stats.dto.StatsDto;

import java.util.List;

public interface StatsService {
    void getFrameStats(String start, String end, List<String> uris, Boolean isUnique);

    void postHitToStats(StatsDto statsDto);
}

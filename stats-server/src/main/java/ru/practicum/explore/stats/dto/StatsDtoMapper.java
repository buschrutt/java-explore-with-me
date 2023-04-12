package ru.practicum.explore.stats.dto;

import ru.practicum.explore.stats.model.Stats;

public class StatsDtoMapper {
    public static StatsDto statsToStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHitsCount())
                .build();
    }
}

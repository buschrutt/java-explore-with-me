package ru.practicum.statistics.stats.dto;

import ru.practicum.domain.StatsDto;
import ru.practicum.statistics.stats.model.Stats;

public class StatsDtoMapper {
    public static StatsDto statsToStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHitsCount())
                .build();
    }
}

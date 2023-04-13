package ru.practicum.stats.mapping;

import ru.practicum.domain.StatsDto;
import ru.practicum.stats.model.Stats;

public class StatsDtoMapper {
    public static StatsDto statsToStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHitsCount())
                .build();
    }
}

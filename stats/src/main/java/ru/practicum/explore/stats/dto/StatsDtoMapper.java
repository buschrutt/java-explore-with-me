package ru.practicum.explore.stats.dto;

import ru.practicum.explore.stats.model.Stats;

import java.time.LocalDateTime;

public class StatsDtoMapper {
    public static Stats dtoToStats(StatsDto statsDto) {
        return Stats.builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .ip(statsDto.getIp())
                .timestamp(LocalDateTime.parse(statsDto.timestamp))
                .build();
    }
}

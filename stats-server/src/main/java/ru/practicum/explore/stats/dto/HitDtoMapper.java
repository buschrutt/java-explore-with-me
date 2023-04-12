package ru.practicum.explore.stats.dto;

import ru.practicum.explore.stats.model.Hit;

import java.time.LocalDateTime;

public class HitDtoMapper {
    public static Hit dtoToHit(HitDto hitDto, LocalDateTime timestamp) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .viewDate(timestamp)
                .build();
    }
}

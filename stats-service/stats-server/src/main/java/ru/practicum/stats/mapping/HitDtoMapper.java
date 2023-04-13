package ru.practicum.stats.mapping;

import ru.practicum.domain.HitDto;
import ru.practicum.stats.model.Hit;

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

package ru.practicum.explore.stats.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import java.time.LocalDateTime;

@Data
@Builder
@PackagePrivate
public class StatsDto {
    Integer id;
    String app;
    String uri;
    String ip;
    String timestamp;
}

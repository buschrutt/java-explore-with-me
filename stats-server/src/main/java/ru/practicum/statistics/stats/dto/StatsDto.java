package ru.practicum.statistics.stats.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@PackagePrivate
public class StatsDto {
    String app;
    String uri;
    long hits;
}

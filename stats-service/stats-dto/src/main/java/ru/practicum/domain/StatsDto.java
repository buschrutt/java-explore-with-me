package ru.practicum.domain;

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

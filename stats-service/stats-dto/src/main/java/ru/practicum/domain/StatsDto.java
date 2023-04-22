package ru.practicum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PackagePrivate
public class StatsDto {
    String app;
    String uri;
    long hits;
}

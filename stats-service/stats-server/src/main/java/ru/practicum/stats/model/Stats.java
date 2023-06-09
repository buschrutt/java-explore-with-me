package ru.practicum.stats.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

@Data
@PackagePrivate
@AllArgsConstructor
public class Stats {
    String app;
    String uri;
    Long hitsCount;
}

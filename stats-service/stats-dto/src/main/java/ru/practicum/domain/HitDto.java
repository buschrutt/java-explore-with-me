package ru.practicum.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@PackagePrivate
public class HitDto {
    Integer id;
    String app;
    String uri;
    String ip;
    String timestamp;
}

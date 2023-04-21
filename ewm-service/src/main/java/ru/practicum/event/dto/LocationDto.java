package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@PackagePrivate
public class LocationDto {
    Float lat;
    Float lon;
}

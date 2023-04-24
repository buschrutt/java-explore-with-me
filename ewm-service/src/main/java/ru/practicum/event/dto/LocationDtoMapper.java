package ru.practicum.event.dto;

import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

public class LocationDtoMapper {
    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}

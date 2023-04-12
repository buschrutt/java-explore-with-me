package ru.practicum.explore.stats.service;

import org.springframework.http.HttpStatus;
import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatsDto> getFrameStats(String start, String end, List<String> uris, Boolean isUnique);

    void postHitToStats(HitDto hitDto);
}

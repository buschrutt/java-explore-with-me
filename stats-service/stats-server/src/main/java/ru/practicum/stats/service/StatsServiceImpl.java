package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;
import ru.practicum.stats.repository.StatsRepository;
import ru.practicum.stats.mapping.HitDtoMapper;
import ru.practicum.stats.mapping.StatsDtoMapper;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean isUnique) {
        List<Stats> statsList = (uris == null) ? (isUnique ? statsRepository.findAllStatsListUniqueIp(start, end)
                : statsRepository.findAllStatsList(start, end))
                : (isUnique ? statsRepository.findStatsListUniqueIp(uris, start, end)
                : statsRepository.findStatsList(uris, start, end));
        return statsList.stream().map(StatsDtoMapper::statsToStatsDto).collect(Collectors.toList());
    }

    @Override
    public void postHitToStats(HitDto hitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(hitDto.getTimestamp(), formatter);
        Hit hit = HitDtoMapper.dtoToHit(hitDto, timestamp);
        statsRepository.save(hit);
    }
}

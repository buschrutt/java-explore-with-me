package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;
import ru.practicum.stats.mapping.HitDtoMapper;
import ru.practicum.stats.mapping.StatsDtoMapper;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<StatsDto> getFrameStats(String start, String end, List<String> uris, Boolean isUnique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Stats> statsList;
        if (uris == null && (isUnique == null || !isUnique)) {
            statsList = statsRepository.findAllStatsList(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        } else if (uris == null) {
            statsList = statsRepository.findAllStatsListUniqueIp(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        } else if (isUnique == null || !isUnique) {
            statsList = statsRepository.findStatsList(uris, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        } else {
            statsList = statsRepository.findStatsListUniqueIp(uris, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
        }
        List<StatsDto> statsDtoList = new ArrayList<>();
        for (Stats stats : statsList) {
            statsDtoList.add(StatsDtoMapper.statsToStatsDto(stats));
        }
        return statsDtoList;
    }

    @Override
    public void postHitToStats(HitDto hitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(hitDto.getTimestamp(), formatter);
        Hit hit = HitDtoMapper.dtoToHit(hitDto, timestamp);
        statsRepository.save(hit);
    }
}

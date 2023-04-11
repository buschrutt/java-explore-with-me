package ru.practicum.explore.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.stats.dto.StatsDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    @Override
    public void getFrameStats(String start, String end, List<String> uris, Boolean isUnique) {

    }

    @Override
    public void postHitToStats(StatsDto statsDto) {


    }
}

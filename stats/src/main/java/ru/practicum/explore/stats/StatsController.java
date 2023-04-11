package ru.practicum.explore.stats;

import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.stats.dto.StatsDto;
import ru.practicum.explore.stats.service.StatsService;

import java.util.List;

@RestController
@RequestMapping
public class StatsController {

    final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public void getFrameStats(@RequestParam(name = "start") String start,
                              @RequestParam(name = "end") String end,
                              @RequestParam(name = "uris", required = false) List<String> uris,
                              @RequestParam(name = "unique", required = false) Boolean isUnique) {
        statsService.getFrameStats(start, end, uris, isUnique);
    }

    @PostMapping("/hit")
    public void postHitToStats(@RequestBody StatsDto statsDto) {
        statsService.postHitToStats(statsDto);
    }
}

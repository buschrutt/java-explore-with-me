package ru.practicum.statistics.stats;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.stats.dto.HitDto;
import ru.practicum.statistics.stats.dto.StatsDto;
import ru.practicum.statistics.stats.service.StatsService;

import java.util.List;

@RestController
@RequestMapping
public class StatsController {

    final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public List<StatsDto> getFrameStats(@RequestParam(name = "start") String start,
                                        @RequestParam(name = "end") String end,
                                        @RequestParam(name = "uris", required = false) List<String> uris,
                                        @RequestParam(name = "unique", required = false) Boolean isUnique) {
        return statsService.getFrameStats(start, end, uris, isUnique);
    }

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postHitToStats(@RequestBody HitDto hitDto) {
        statsService.postHitToStats(hitDto);
    }
}

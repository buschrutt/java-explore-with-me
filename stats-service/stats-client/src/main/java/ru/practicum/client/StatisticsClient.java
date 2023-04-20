package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;

import java.util.List;
import java.util.Objects;

@Service
public class StatisticsClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        ResponseEntity<StatsDto> responseEntity = restTemplate.getForEntity("http://localhost:9090/stats?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + unique,
                StatsDto.class);
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

    public void postHit(HitDto hitDto) {
        restTemplate.postForLocation("http://localhost:9090/hit", hitDto);
    }

}

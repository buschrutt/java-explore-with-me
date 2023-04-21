package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class StatisticsClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        String urisParam = String.join(",", uris);
        String url = "http://localhost:9090/stats?start=" + start + "&end=" + end + "&uris=" + urisParam + "&unique=" + unique;
        ResponseEntity<StatsDto[]> responseEntity = restTemplate.getForEntity(url, StatsDto[].class);
        StatsDto[] responseArray = responseEntity.getBody();
        if (responseArray != null && responseArray.length > 0) {
            return List.of(responseArray);
        } else {
            return Collections.emptyList();
        }
    }

    public void postHit(HitDto hitDto) {
        restTemplate.postForLocation("http://localhost:9090/hit", hitDto);
    }

}

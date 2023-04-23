package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.domain.HitDto;
import ru.practicum.domain.StatsDto;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsClient {
    @Value("${server.uri}")
    private String localAddress;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        String urisParam = String.join(",", uris);
        String url = localAddress + "/stats?start=" + start + "&end=" + end + "&uris=" + urisParam + "&unique=" + unique;
        ResponseEntity<StatsDto[]> responseEntity = restTemplate.getForEntity(url, StatsDto[].class);
        StatsDto[] responseArray = responseEntity.getBody();
        if (responseArray != null && responseArray.length > 0) {
            return List.of(responseArray);
        } else {
            return Collections.emptyList();
        }
    }

    public void postHit(HitDto hitDto) {
        restTemplate.postForLocation(localAddress + "/hit", hitDto);
    }

}
